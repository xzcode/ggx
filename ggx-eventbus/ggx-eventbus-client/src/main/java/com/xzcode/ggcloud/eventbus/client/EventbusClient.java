package com.xzcode.ggcloud.eventbus.client;

import java.nio.charset.Charset;
import java.util.List;

import com.ggx.core.client.GGClient;
import com.ggx.core.client.config.GGClientConfig;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.handler.serializer.ISerializer;
import com.ggx.core.common.message.response.support.IMakePackSupport;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.ISessionManager;
import com.ggx.core.common.utils.GenericClassUtil;
import com.ggx.core.common.utils.RandomIdUtil;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.xzcode.ggcloud.eventbus.client.config.EventbusClientConfig;
import com.xzcode.ggcloud.eventbus.client.handler.EventMessageRespHandler;
import com.xzcode.ggcloud.eventbus.client.handler.EventPublishRespHandler;
import com.xzcode.ggcloud.eventbus.client.handler.EventSubscribeRespHandler;
import com.xzcode.ggcloud.eventbus.client.subscriber.Subscriber;
import com.xzcode.ggcloud.eventbus.client.subscriber.SubscriberInfo;
import com.xzcode.ggcloud.eventbus.client.subscriber.SubscriberManager;
import com.xzcode.ggcloud.eventbus.common.constant.EventbusConstant;
import com.xzcode.ggcloud.eventbus.common.message.req.EventPublishReq;
import com.xzcode.ggcloud.eventbus.common.message.req.EventSubscribeReq;
import com.xzcode.ggcloud.eventbus.common.message.resp.EventMessageResp;
import com.xzcode.ggcloud.eventbus.common.message.resp.EventPublishResp;
import com.xzcode.ggcloud.eventbus.common.message.resp.EventSubscribeResp;
import com.xzcode.ggcloud.session.group.client.SessionGroupClient;
import com.xzcode.ggcloud.session.group.client.config.SessionGroupClientConfig;
import com.xzcode.ggcloud.session.group.client.session.ServiceClientSession;
import com.xzcode.ggcloud.session.group.common.constant.GGSessionGroupEventConstant;

public class EventbusClient{
	
	private EventbusClientConfig config;
	
	private GGClient serviceClient;
	
	
	public EventbusClient(EventbusClientConfig config) {
		this.config = config;
		this.config.setEventbusClient(this);
		init();
	}

	public void init() {
		
		SessionGroupClientConfig sessionGroupClientConfig = new SessionGroupClientConfig();
		sessionGroupClientConfig.setEnableServiceClient(true);
		sessionGroupClientConfig.setAuthToken(this.config.getAuthToken());
		sessionGroupClientConfig.setWorkThreadFactory(new GGThreadFactory("gg-evt-cli-", false));
		sessionGroupClientConfig.setConnectionSize(this.config.getConnectionSize());
		sessionGroupClientConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		
		SessionGroupClient sessionGroupClient = new SessionGroupClient(sessionGroupClientConfig);
		
		//添加会话注册成功监听
		sessionGroupClient.addEventListener(GGSessionGroupEventConstant.SESSION_REGISTER_SUCCESS, e -> {
			SubscriberManager subscribeManager = this.config.getSubscribeManager();
			//获取待注册的事件id集合
			List<String> eventIds = subscribeManager.getEventIdList();
			
			//发送订阅请求
			EventSubscribeReq req = new EventSubscribeReq(eventIds);
			
			GGClientConfig serviceClientConfig = this.config.getSessionGroupClient().getConfig().getServiceClient().getConfig();
			ISessionManager sessionManager = serviceClientConfig.getSessionManager();
			
			ServiceClientSession serviceClientSession = new ServiceClientSession(RandomIdUtil.newRandomStringId24(), sessionGroupClientConfig.getSessionGroupId(), sessionGroupClientConfig.getSessionGroupManager(), serviceClientConfig );
			
			GGSession addSessionIfAbsent = sessionManager.addSessionIfAbsent(serviceClientSession);
			if (addSessionIfAbsent != null) {
				serviceClientSession = (ServiceClientSession) addSessionIfAbsent;
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
			publishReq.setEventData(this.serviceClient.getConfig().getSerializer().serialize(data));
			publishReq.setEventId(eventId);
			publishReq.setSubscriberId(data.getClass().getName());
			
			ISessionManager sessionManager = this.serviceClient.getSessionManager();
			GGSession session = sessionManager.randomGetSession();
			if (session != null) {
				session.send(publishReq);
			}
			
		} catch (Exception e) {
			GGLoggerUtil.getLogger(this).error("Eventbus publish event ERROR!", e);
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
	public <T> void subscribe(String eventId, Subscriber<T> subscriber) {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		Class<?> subscriberClass = GenericClassUtil.getGenericClass(subscriber.getClass());
		subscriberInfo.setClazz(subscriberClass);
		subscriberInfo.setSubscriber(subscriber);
		subscriberInfo.setSubscriberId(subscriberClass.getName());
		
		this.config.getSubscribeManager().subscribe(eventId, subscriberInfo);
	}

}
