package com.ggx.group.server.handler;

import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.common.message.req.SessionGroupRegisterReq;
import com.ggx.group.common.message.resp.SessionGroupRegisterResp;
import com.ggx.group.server.config.SessionGroupServerConfig;

/**
 * 内置ping处理器
 * 
 * @author zai
 * 2020-01-16 17:04:11
 */
public class SessionGroupRegisterReqHandler {
	
	protected SessionGroupServerConfig config;
	
	public SessionGroupRegisterReqHandler(SessionGroupServerConfig config) {
		this.config = config;
	}

	@GGXAction
	public void handle(SessionGroupRegisterReq req, GGXSession groupSession) {
		String groupId = req.getGroupId();
		GGSessionGroupManager sessionGroupManager = config.getSessionGroupManager();
		
		groupSession.setReady(true);
		sessionGroupManager.addSession(groupId, groupSession);
		
		
		groupSession.send(new SessionGroupRegisterResp(true));
		
	}



}
