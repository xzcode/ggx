package com.xzcode.ggcloud.eventbus.server.handler;

import com.xzcode.ggcloud.eventbus.common.message.req.EventPublishReq;
import com.xzcode.ggcloud.eventbus.common.message.resp.EventMessageResp;
import com.xzcode.ggcloud.eventbus.server.config.EventbusServerConfig;
import com.xzcode.ggserver.core.common.message.MessageData;
import com.xzcode.ggserver.core.common.message.request.action.MessageDataHandler;

/**
 * 事件发布请求
 *
 * @author zai
 * 2020-04-10 14:49:48
 */
public class EventPublishReqHandler implements MessageDataHandler<EventPublishReq>{
	
	private EventbusServerConfig config;
	

	public EventPublishReqHandler(EventbusServerConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<EventPublishReq> messageData) {
		EventPublishReq req = messageData.getMessage();
		String eventId = req.getEventId();
		String subscriberId = req.getSubscriberId();
		byte[] eventData = req.getEventData();
		
		EventMessageResp resp = new EventMessageResp(eventId, subscriberId, eventData);
		this.config.getSubscriptionManager().publish(resp);
	}

	

}
