package com.xzcode.ggcloud.eventbus.server.handler;

import java.util.List;

import com.ggx.common.message.req.EventSubscribeReq;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.xzcode.ggcloud.eventbus.server.config.EventbusServerConfig;

/**
 * 客户端认证请求
 *
 * @author zai
 * 2020-04-07 10:57:11
 */
public class EventSubscribeReqHandler implements MessageHandler<EventSubscribeReq>{
	
	private EventbusServerConfig config;
	

	public EventSubscribeReqHandler(EventbusServerConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<EventSubscribeReq> messageData) {
		EventSubscribeReq req = messageData.getMessage();
		List<String> eventIds = req.getEventIds();
		GGSession session = messageData.getSession();
		//添加监听
		this.config.getSubscriptionManager().addSubscription(eventIds, session);
	}

	

}
