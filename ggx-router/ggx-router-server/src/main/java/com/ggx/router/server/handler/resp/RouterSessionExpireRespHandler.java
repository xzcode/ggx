package com.ggx.router.server.handler.resp;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.router.common.message.resp.RouterSessionExpireResp;
import com.ggx.router.server.config.RouterServerConfig;

/**
 * 路由会话过期请求处理器
 *
 * @author zai
 * 2020-05-11 14:49:47
 */
public class RouterSessionExpireRespHandler implements MessageDataHandler<RouterSessionExpireResp>{
	
	private RouterServerConfig config;
	

	public RouterSessionExpireRespHandler(RouterServerConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<RouterSessionExpireResp> messageData) {
		GGSession session = messageData.getSession();
		
		RouterSessionExpireResp resp = messageData.getMessage();
		
		
	}
	

}
