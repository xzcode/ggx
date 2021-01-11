package com.ggx.eventbus.client;

import java.util.List;

import com.ggx.common.controller.req.EventPublishReq;
import com.ggx.common.controller.req.EventSubscribeReq;
import com.ggx.common.message.EventbusMessage;
import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.serializer.factory.SerializerFactory;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.eventbus.client.config.EventbusClientConfig;
import com.ggx.eventbus.client.controller.EventbusClientController;
import com.ggx.eventbus.client.subscriber.SubscriberManager;
import com.ggx.eventbus.client.subscriber.impl.DefaultSubscriberManager;
import com.ggx.group.common.constant.GGSessionGroupEventConstant;
import com.ggx.session.group.client.SessionGroupClient;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.thread.GGXThreadFactory;

public class EventbusClient{
	
	private EventbusClientConfig config;
	
	private GGXCoreClient serviceClient;
	
	private Serializer serializer = SerializerFactory.KRYO_SERIALIZER;
	
	
	public EventbusClient(EventbusClientConfig config) {
		this.config = config;
		this.config.setEventbusClient(this);
		init();
	}

	public void init() {
		
		if (this.config.getSubscribeManager() == null) {
			this.config.setSubscribeManager(new DefaultSubscriberManager());
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
		
		this.serviceClient = sessionGroupClientConfig.getServiceClient();
		
		this.config.setSessionGroupClient(sessionGroupClient);
		
		this.serviceClient.registerMessageController(new EventbusClientController(config));
		
		//添加会话注册成功监听
		sessionGroupClient.addEventListener(GGSessionGroupEventConstant.SESSION_REGISTER_SUCCESS, e -> {
			GGXSession groupSession = e.getSession();
			SubscriberManager subscribeManager = this.config.getSubscribeManager();
			//获取待注册的事件id集合
			List<String> eventIds = subscribeManager.getEventIdList();
			
			//发送订阅请求
			EventSubscribeReq req = new EventSubscribeReq(eventIds);
			
			GGXCoreClientConfig serviceClientConfig = this.serviceClient.getConfig();
			SessionManager sessionManager = serviceClientConfig.getSessionManager();
			GGXSession serviceClientSession = sessionManager.getSession(groupSession.getSessionId());
			
			serviceClientSession.send(req);
			
		});
		
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
	public void publishEvent(String eventId, EventbusMessage data) {
		try {
			
			EventPublishReq publishReq = new EventPublishReq();
			publishReq.setEventId(eventId);
			if (data != null) {
				publishReq.setEventData(this.serializer.serialize(data));
			}
			
			SessionManager sessionManager = this.serviceClient.getSessionManager();
			GGXSession session = sessionManager.getRandomSession();
			if (session != null) {
				session.send(publishReq);
			}
			
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Eventbus publish event ERROR!", e);
		}
	}
	public void publishEvent(EventbusMessage message) {
		String eventId = config.getSubscribeManager().getEventId(message.getClass());
		if (eventId == null) {
			
		}
		publishEvent(eventId, message);
	}

	/**
	 * 注册事件订阅控制器
	 *
	 * @param controllerObj
	 * @author zai
	 * 2020-10-17 16:10:11
	 */
	public void registerSubscriberController(Object controllerObj) {
		this.config.getSubscribeManager().registerSubscriberController(controllerObj);
	}

	public void shutdown() {
		this.config.getSessionGroupClient().shutdown(false);
		
	}

}
