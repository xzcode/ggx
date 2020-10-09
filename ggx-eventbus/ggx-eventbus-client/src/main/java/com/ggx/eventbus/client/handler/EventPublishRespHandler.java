package com.ggx.eventbus.client.handler;

import com.ggx.common.message.resp.EventPublishResp;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.eventbus.client.config.EventbusClientConfig;

/**
 * 事件发布响应
 *
 * @author zai
 * 2020-04-10 14:52:35
 */
public class EventPublishRespHandler{
	
	protected EventbusClientConfig config;
	

	public EventPublishRespHandler(EventbusClientConfig config) {
		super();
		this.config = config;
	}



	@GGXAction
	public void handle(EventPublishResp resp) {
	}

	

}
