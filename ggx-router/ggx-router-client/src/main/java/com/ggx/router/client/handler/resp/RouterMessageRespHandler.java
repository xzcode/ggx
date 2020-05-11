package com.ggx.router.client.handler.resp;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.common.message.resp.RouterMessageResp;

/**
 * 路由消息推送
 *
 * @author zai
 * 2020-05-11 15:31:16
 */
public class RouterMessageRespHandler implements MessageDataHandler<RouterMessageResp>{
	
	private RouterClientConfig config;
	

	public RouterMessageRespHandler(RouterClientConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<RouterMessageResp> messageData) {
		GGSession session = messageData.getSession();
		
		
		
		
	}
	

}
