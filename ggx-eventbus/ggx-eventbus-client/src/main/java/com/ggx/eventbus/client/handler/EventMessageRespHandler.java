package com.ggx.eventbus.client.handler;

import com.ggx.common.message.resp.EventMessageResp;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.eventbus.client.config.EventbusClientConfig;
import com.ggx.eventbus.client.subscriber.SubscriberGroup;
import com.ggx.eventbus.client.subscriber.SubscriberManager;
import com.ggx.eventbus.client.subscriber.SubscriptionData;

/**
 * 消息接收处理器
 *
 * @author zai
 * 2020-04-07 11:37:01
 */
public class EventMessageRespHandler implements MessageHandler<EventMessageResp>{
	
	private EventbusClientConfig config;
	
	private Serializer serializer;
	

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
			byte[] eventData = resp.getEventData();
			
			SubscriberManager subscribeManager = this.config.getSubscribeManager();
			
			SubscriberGroup subscriberGroup = subscribeManager.getSubscriberGroup(eventId);
			if (subscriberGroup != null) {
				subscriberGroup.trigger(new SubscriptionData(eventId, eventData, this.serializer));
			}
			
		} catch (Exception e) {
			GGLoggerUtil.getLogger(this).error("Eventbus receive message ERROR!", e);
		}
	}

	

}
