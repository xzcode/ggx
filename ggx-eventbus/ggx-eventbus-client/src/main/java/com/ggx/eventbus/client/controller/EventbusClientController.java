package com.ggx.eventbus.client.controller;

import com.ggx.common.message.resp.EventMessageResp;
import com.ggx.common.message.resp.EventPublishResp;
import com.ggx.common.message.resp.EventSubscribeResp;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.serializer.factory.SerializerFactory;
import com.ggx.core.common.serializer.factory.SerializerFactory.SerializerType;
import com.ggx.eventbus.client.config.EventbusClientConfig;
import com.ggx.eventbus.client.subscriber.SubscriberManager;
import com.ggx.eventbus.client.subscriber.group.SubscriberGroup;
import com.ggx.eventbus.client.subscriber.model.SubscriptionData;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 消息接收处理器
 *
 * @author zai
 * 2020-04-07 11:37:01
 */
public class EventbusClientController{
	
	private EventbusClientConfig config;
	
	private Serializer serializer = SerializerFactory.KRYO_SERIALIZER;
	

	public EventbusClientController(EventbusClientConfig config) {
		super();
		this.config = config;
		this.serializer = this.config.getSessionGroupClient().getConfig().getSessionClient().getSerializer();
	}



	@GGXAction
	public void eventMessageResp(EventMessageResp resp) {
		try {
			String eventId = resp.getEventId();
			byte[] eventData = resp.getEventData();
			
			SubscriberManager subscribeManager = this.config.getSubscribeManager();
			
			SubscriberGroup subscriberGroup = subscribeManager.getSubscriberGroup(eventId);
			if (subscriberGroup != null) {
				Object data = null;
				if (eventData != null && eventData.length > 0) {
					data = serializer.deserialize(eventData, subscriberGroup.getDataType());
				}
				subscriberGroup.trigger(new SubscriptionData(eventId, data));
			}
			
		} catch (Throwable e) {
			GGXLogUtil.getLogger(this).error("Eventbus receive message ERROR!", e);
		}
	}
	
	@GGXAction
	public void eventPublishResp(EventPublishResp resp) {
	}

	
	@GGXAction
	public void eventSubscribeResp(EventSubscribeResp resp) {
	}


	

}
