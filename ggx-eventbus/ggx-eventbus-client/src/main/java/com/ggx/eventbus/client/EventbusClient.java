package com.ggx.eventbus.client;

import java.util.List;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.common.message.req.EventPublishReq;
import com.ggx.common.message.req.EventSubscribeReq;
import com.ggx.common.message.resp.EventMessageResp;
import com.ggx.common.message.resp.EventPublishResp;
import com.ggx.common.message.resp.EventSubscribeResp;
import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.common.utils.GGXIdUtil;
import com.ggx.eventbus.client.config.EventbusClientConfig;
import com.ggx.eventbus.client.handler.EventMessageRespHandler;
import com.ggx.eventbus.client.handler.EventPublishRespHandler;
import com.ggx.eventbus.client.handler.EventSubscribeRespHandler;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.eventbus.client.subscriber.SubscriberManager;
import com.ggx.group.common.constant.GGSessionGroupEventConstant;
import com.ggx.session.group.client.SessionGroupClient;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.ggx.session.group.client.session.GroupServiceClientSession;
import com.ggx.util.logger.GGXLogUtil;

public class EventbusClient{
	
	private EventbusClientConfig config;
	
	private GGXCoreClient serviceClient;
	
	
	public EventbusClient(EventbusClientConfig config) {
		this.config = config;
		this.config.setEventbusClient(this);
		init();
	}

	public void init() {
		
		if (this.config.getSubscribeManager() == null) {
			this.config.setSubscribeManager(new SubscriberManager());
		}
		
		
		
		SessionGroupClientConfig sessionGroupClientConfig = new SessionGroupClientConfig();
		sessionGroupClientConfig.setEnableServiceClient(true);
		sessionGroupClientConfig.setAuthToken(this.config.getAuthToken());
		sessionGroupClientConfig.setWorkThreadFactory(new GGXThreadFactory("ggx-evt-cli-", false));
		sessionGroupClientConfig.setConnectionSize(this.config.getConnectionSize());
		sessionGroupClientConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionGroupClientConfig.setServerHost(this.config.getServerHost());
		sessionGroupClientConfig.setServerPort(this.config.getServerPort());
		
		if (this.config.getSharedEventLoopGroup() != null) {
			sessionGroupClientConfig.setWorkEventLoopGroup(this.config.getSharedEventLoopGroup());
		}
		
		SessionGroupClient sessionGroupClient = new SessionGroupClient(sessionGroupClientConfig);
		
		//添加会话注册成功监听
		sessionGroupClient.addEventListener(GGSessionGroupEventConstant.SESSION_REGISTER_SUCCESS, e -> {
			SubscriberManager subscribeManager = this.config.getSubscribeManager();
			//获取待注册的事件id集合
			List<String> eventIds = subscribeManager.getEventIdList();
			
			//发送订阅请求
			EventSubscribeReq req = new EventSubscribeReq(eventIds);
			
			GGXCoreClientConfig serviceClientConfig = this.config.getSessionGroupClient().getConfig().getServiceClient().getConfig();
			SessionManager sessionManager = serviceClientConfig.getSessionManager();
			
			GroupServiceClientSession serviceClientSession = new GroupServiceClientSession(GGXIdUtil.newRandomStringId24(), sessionGroupClientConfig.getSessionGroupId(), sessionGroupClientConfig.getSessionGroupManager(), serviceClientConfig );
			
			GGXSession addSessionIfAbsent = sessionManager.addSessionIfAbsent(serviceClientSession);
			if (addSessionIfAbsent != null) {
				serviceClientSession = (GroupServiceClientSession) addSessionIfAbsent;
			}
			
			serviceClientSession.send(req);
			
		});
		
		this.serviceClient = sessionGroupClientConfig.getServiceClient();
		
		this.config.setSessionGroupClient(sessionGroupClient);
		
		
		
		this.serviceClient.onMessage(EventPublishResp.ACTION_ID, new EventPublishRespHandler(config));
		this.serviceClient.onMessage(EventSubscribeResp.ACTION_ID, new EventSubscribeRespHandler(config));
		this.serviceClient.onMessage(EventMessageResp.ACTION_ID, new EventMessageRespHandler(config));
		
		
		//包日志输出控制
		if (!this.config.isPrintEventbusPackLog()) {
			this.serviceClient.getConfig().getPackLogger().addPackLogFilter(pack -> {
				String actionString = pack.getActionString();
				return !(actionString.startsWith(EventbusConstant.ACTION_ID_PREFIX));
			});
		}
		
		
		
	}
	
	public void start() {
		this.config.getSessionGroupClient().start();
	}
	
	/**
	 * 发布事件消息
	 *
	 * @param eventId
	 * @author zai
	 * 2020-04-11 18:12:48
	 */
	public void publishEvent(String eventId, Object data) {
		try {
			
			EventPublishReq publishReq = new EventPublishReq();
			publishReq.setEventId(eventId);
			if (data != null) {
				publishReq.setEventData(this.serviceClient.getConfig().getSerializer().serialize(data));
			}
			
			SessionManager sessionManager = this.serviceClient.getSessionManager();
			GGXSession session = sessionManager.randomGetSession();
			if (session != null) {
				session.send(publishReq);
			}
			
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Eventbus publish event ERROR!", e);
		}
	}

	/**
	 * 注册事件订阅
	 *
	 * @param <T>
	 * @param eventId
	 * @param subscriber
	 * @author zai
	 * 2020-04-11 22:54:45
	 */
	public void subscribe(String eventId, Subscriber subscriber) {
		this.config.getSubscribeManager().subscribe(eventId, subscriber);
	}

	public void shutdown() {
		this.config.getSessionGroupClient().shutdown(false);
		
	}

}
