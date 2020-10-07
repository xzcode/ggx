package com.ggx.eventbus.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.common.message.req.EventPublishReq;
import com.ggx.common.message.resp.EventMessageResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.handler.MessageHandler;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.google.gson.Gson;

/**
 * 事件发布请求
 *
 * @author zai
 * 2020-04-10 14:49:48
 */
public class EventPublishReqHandler implements MessageHandler<EventPublishReq>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventPublishReqHandler.class);
	
	private static final Gson GSON = new Gson();
	
	private EventbusServerConfig config;
	

	public EventPublishReqHandler(EventbusServerConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<EventPublishReq> messageData) {
		EventPublishReq req = messageData.getMessage();
		String eventId = req.getEventId();
		byte[] eventData = req.getEventData();
		
		EventMessageResp resp = new EventMessageResp(eventId, eventData);
		this.config.getSubscriptionManager().publish(resp);
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\nPublish Event ['{}'] ", GSON.toJson(req));
		}
		
	}

	

}
