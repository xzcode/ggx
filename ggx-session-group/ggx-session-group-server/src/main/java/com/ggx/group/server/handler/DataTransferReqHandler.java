package com.ggx.group.server.handler;

import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.group.common.message.req.DataTransferReq;
import com.ggx.group.server.config.SessionGroupServerConfig;

/**
 * 客户端认证请求
 *
 * @author zai 2020-04-07 10:57:11
 */
public class DataTransferReqHandler  {

	private SessionGroupServerConfig config;

	public DataTransferReqHandler(SessionGroupServerConfig config) {
		this.config = config;
	}

	@GGXAction
	public void handle(DataTransferReq req, GGXSession groupSession) {
		String groupSessionId = groupSession.getSessionId();
		
		GGXCoreServer serviceServer = config.getServiceServer();
		GGXCoreServerConfig serviceServerConfig = serviceServer.getConfig();
		
		SessionManager serviceSessionManager = serviceServerConfig.getSessionManager();
		GGXSession serviceSession = serviceSessionManager.getSession(groupSessionId);
		
		serviceServerConfig.getReceiveMessageManager().receive(new Pack(serviceSession, req.getAction(), req.getMessage()));
		
			
	}

}
