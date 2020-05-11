package com.ggx.router.server.handler.req;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.common.message.req.RouterSessionExpireReq;
import com.ggx.router.server.config.RouterServerConfig;

/**
 * 路由会话过期请求处理器
 *
 * @author zai
 * 2020-05-11 14:49:47
 */
public class RouterSessionExpireReqHandler implements MessageDataHandler<RouterSessionExpireReq>{
	
	private RouterServerConfig config;
	

	public RouterSessionExpireReqHandler(RouterServerConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<RouterSessionExpireReq> messageData) {
		GGSession session = messageData.getSession();
		
		RouterSessionExpireReq req = messageData.getMessage();
		String routeSessionId = req.getRouteSessionId();
		
		SessionManager sessionManager = this.config.getServiceServer().getSessionManager();
		GGSession serviceSession = sessionManager.getSession(routeSessionId);
		if (serviceSession != null) {
			serviceSession.disconnect();
		}
		
		if (this.config.isEnableForwardRouterClient()) {
			RouterClient forwardRouterClient = this.config.getForwardRouterClient();
			RouterClientConfig forwardRouterClientConfig = forwardRouterClient.getConfig();
		}
		
	}
	

}
