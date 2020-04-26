package com.ggx.eventbus.client.handler;

import com.ggx.common.message.resp.EventMessageResp;
import com.ggx.common.message.resp.EventSubscribeResp;
import com.ggx.core.common.handler.serializer.ISerializer;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.eventbus.client.config.EventbusClientConfig;
import com.ggx.eventbus.client.subscriber.SubscriberInfo;
import com.ggx.eventbus.client.subscriber.SubscriberManager;

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
