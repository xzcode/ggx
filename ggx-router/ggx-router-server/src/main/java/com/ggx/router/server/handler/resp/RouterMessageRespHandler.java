package com.ggx.router.server.handler.resp;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.message.request.task.MessageDataTask;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.group.server.session.GroupServiceServerSession;
import com.ggx.router.client.RouterClient;
import com.ggx.router.common.message.req.RouterMessageReq;
import com.ggx.router.common.message.resp.RouterMessageResp;
import com.ggx.router.server.config.RouterServerConfig;
import com.ggx.router.server.session.RouterServiceServerSession;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

/**
 * 路由消息推送
 *
 * @author zai
 * 2020-05-11 15:31:16
 */
public class RouterMessageRespHandler implements MessageDataHandler<RouterMessageResp>{
	
	private RouterServerConfig config;
	

	public RouterMessageRespHandler(RouterServerConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<RouterMessageResp> messageData) {
		GGSession session = messageData.getSession();
		
		
	}
	

}
