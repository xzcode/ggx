package com.xzcode.ggcloud.eventbus.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.xzcode.ggcloud.eventbus.client.config.EventbusClientConfig;
import com.xzcode.ggcloud.eventbus.common.message.resp.EventPublishResp;

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
