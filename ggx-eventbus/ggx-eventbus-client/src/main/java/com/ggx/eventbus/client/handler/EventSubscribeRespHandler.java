package com.ggx.eventbus.client.handler;

import com.ggx.common.message.resp.AuthResp;
import com.ggx.common.message.resp.EventSubscribeResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.eventbus.client.config.EventbusClientConfig;

/**
 * 客户端认证请求
 *
 * @author zai
 * 2020-04-07 10:57:11
 */
public class EventSubscribeRespHandler implements MessageDataHandler<EventSubscribeResp>{
	
	private EventbusClientConfig config;
	

	public EventSubscribeRespHandler(EventbusClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<EventSubscribeResp> request) {
		EventSubscribeResp resp = request.getMessage();
	}

	

}
