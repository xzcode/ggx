package com.xzcode.ggcloud.eventbus.client.handler;

import com.xzcode.ggcloud.eventbus.client.config.EventbusClientConfig;
import com.xzcode.ggcloud.eventbus.client.subscriber.SubscriberInfo;
import com.xzcode.ggcloud.eventbus.client.subscriber.SubscriberManager;
import com.xzcode.ggcloud.eventbus.common.message.resp.EventMessageResp;
import com.xzcode.ggcloud.eventbus.common.message.resp.EventSubscribeResp;
import com.xzcode.ggserver.core.common.handler.serializer.ISerializer;
import com.xzcode.ggserver.core.common.message.MessageData;
import com.xzcode.ggserver.core.common.message.request.action.MessageDataHandler;
import com.xzcode.ggserver.core.common.utils.logger.GGLoggerUtil;

/**
 * 消息接收处理器
 *
 * @author zai
 * 2020-04-07 11:37:01
 */
public class EventMessageRespHandler implements MessageDataHandler<EventMessageResp>{
	
	private EventbusClientConfig config;
	
	private ISerializer serializer;
	

	public EventMessageRespHandler(EventbusClientConfig config) {
		super();
		this.config = config;
		this.serializer = this.config.getSessionGroupClient().getConfig().getSessionClient().getSerializer();
	}



	@Override
	public void handle(MessageData<EventMessageResp> messageData) {
		try {
			EventMessageResp resp = messageData.getMessage();
			String eventId = resp.getEventId();
			String subscriberId = resp.getSubscriberId();
			byte[] eventData = resp.getEventData();
			
			
			SubscriberManager subscribeManager = this.config.getSubscribeManager();
			SubscriberInfo subscriberInfo = subscribeManager.getSubscriberInfo(eventId, subscriberId);
			Class<?> clazz = subscriberInfo.getClazz();
			Object data = this.serializer.deserialize(eventData, clazz);
			subscribeManager.trigger(eventId, subscriberId, data);
		} catch (Exception e) {
			GGLoggerUtil.getLogger(this).error("Eventbus receive message ERROR!", e);
		}
	}

	

}
