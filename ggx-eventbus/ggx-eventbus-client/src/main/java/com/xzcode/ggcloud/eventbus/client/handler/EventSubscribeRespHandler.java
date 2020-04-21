package com.xzcode.ggcloud.eventbus.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.xzcode.ggcloud.eventbus.client.config.EventbusClientConfig;
import com.xzcode.ggcloud.eventbus.common.message.resp.AuthResp;
import com.xzcode.ggcloud.eventbus.common.message.resp.EventSubscribeResp;

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
