package com.ggx.router.client.service.handler.resp;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.common.message.resp.RouterMessageResp;
import com.xzcode.ggserver.core.server.GGServer;

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
		RouterMessageResp resp = messageData.getMessage();
		String routeSessionId = resp.getRouteSessionId();
		
		//找到业务服务端对象，并获取关联session，进行消息推送
		GGServer hostServer = config.getHostServer();
		SessionManager hostSessionManager = hostServer.getSessionManager();
		GGSession hostSession = hostSessionManager.getSession(routeSessionId);
		if (hostSession != null) {
			hostSession.send(new Pack(hostSession, resp.getAction(), resp.getMessage()));
		}
		
		
		
	}
	

}
