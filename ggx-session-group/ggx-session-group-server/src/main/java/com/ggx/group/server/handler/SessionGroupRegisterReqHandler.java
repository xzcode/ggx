package com.ggx.group.server.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGXSession;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.common.message.req.SessionGroupRegisterReq;
import com.ggx.group.common.message.resp.SessionGroupRegisterResp;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.group.server.constant.SessionGroupServerSessionKeys;

/**
 * 内置ping处理器
 * 
 * @author zai
 * 2020-01-16 17:04:11
 */
public class SessionGroupRegisterReqHandler implements MessageHandler<SessionGroupRegisterReq> {
	
	protected SessionGroupServerConfig config;
	
	public SessionGroupRegisterReqHandler(SessionGroupServerConfig config) {
		this.config = config;
	}

	@Override
	public void handle(MessageData<SessionGroupRegisterReq> request) {
		GGXSession session = request.getSession();
		SessionGroupRegisterReq req = request.getMessage();
		String groupId = req.getGroupId();
		GGSessionGroupManager sessionGroupManager = config.getSessionGroupManager();
		
		session.addAttribute(SessionGroupServerSessionKeys.GROUP_SESSION_GROUP_ID, groupId);
		session.setReady(true);
		sessionGroupManager.addSession(groupId, session);
		
		session.send(new SessionGroupRegisterResp(true));
		
	}



}
