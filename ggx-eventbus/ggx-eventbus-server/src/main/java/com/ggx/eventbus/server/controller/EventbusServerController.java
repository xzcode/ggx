package com.ggx.eventbus.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.common.controller.req.EventPublishReq;
import com.ggx.common.controller.req.EventSubscribeReq;
import com.ggx.common.controller.resp.EventMessageResp;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.ggx.eventbus.server.subscription.SubscriptionManager;
import com.google.gson.Gson;

/**
 * 事件发布请求
 *
 * @author zai
 * 2020-04-10 14:49:48
 */
public class EventbusServerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventbusServerController.class);
	
	private static final Gson GSON = new Gson();
	
	private EventbusServerConfig config;
	
	private SubscriptionManager subscriptionManager;
	

	public EventbusServerController(EventbusServerConfig config) {
		this.config = config;
		this.subscriptionManager = this.config.getSubscriptionManager();
	}



	@GGXAction
	public void eventPublishReq(EventPublishReq req) {
		String eventId = req.getEventId();
		byte[] eventData = req.getEventData();
		
		EventMessageResp resp = new EventMessageResp(eventId, eventData);
		this.subscriptionManager.publish(resp);
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\nPublish Event ['{}'] ", GSON.toJson(req));
		}
	}
	
	@GGXAction
	public void eventSubscribeReq(EventSubscribeReq req, GGXSession session) {
		List<String> eventIds = req.getEventIds();
		//添加监听
		this.subscriptionManager.addSubscription(eventIds, session);
	}


	

}
