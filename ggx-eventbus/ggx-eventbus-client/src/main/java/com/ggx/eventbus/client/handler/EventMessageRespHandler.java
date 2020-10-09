package com.ggx.eventbus.client.handler;

import com.ggx.common.message.resp.EventMessageResp;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.eventbus.client.config.EventbusClientConfig;
import com.ggx.eventbus.client.subscriber.SubscriberGroup;
import com.ggx.eventbus.client.subscriber.SubscriberManager;
import com.ggx.eventbus.client.subscriber.SubscriptionData;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 消息接收处理器
 *
 * @author zai
 * 2020-04-07 11:37:01
 */
public class EventMessageRespHandler{
	
	private EventbusClientConfig config;
	
	private Serializer serializer;
	

	public EventMessageRespHandler(EventbusClientConfig config) {
		super();
		this.config = config;
		this.serializer = this.config.getSessionGroupClient().getConfig().getSessionClient().getSerializer();
	}



	@GGXAction
	public void handle(EventMessageResp resp) {
		try {
			String eventId = resp.getEventId();
			byte[] eventData = resp.getEventData();
			
			SubscriberManager subscribeManager = this.config.getSubscribeManager();
			
			SubscriberGroup subscriberGroup = subscribeManager.getSubscriberGroup(eventId);
			if (subscriberGroup != null) {
				subscriberGroup.trigger(new SubscriptionData(eventId, eventData, this.serializer));
			}
			
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Eventbus receive message ERROR!", e);
		}
	}

	

}
