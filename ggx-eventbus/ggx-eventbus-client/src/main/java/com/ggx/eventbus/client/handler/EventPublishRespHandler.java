package com.ggx.eventbus.client.handler;

import com.ggx.common.message.resp.EventPublishResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.eventbus.client.config.EventbusClientConfig;

/**
 * 事件发布响应
 *
 * @author zai
 * 2020-04-10 14:52:35
 */
public class EventPublishRespHandler implements MessageDataHandler<EventPublishResp>{
	
	private EventbusClientConfig config;
	

	public EventPublishRespHandler(EventbusClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<EventPublishResp> request) {
		//EventPublishResp resp = request.getMessage();
	}

	

}
