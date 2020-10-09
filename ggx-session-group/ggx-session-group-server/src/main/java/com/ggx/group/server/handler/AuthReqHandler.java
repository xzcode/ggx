package com.ggx.group.server.handler;

import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.group.common.message.req.AuthReq;
import com.ggx.group.common.message.resp.AuthResp;
import com.ggx.group.server.config.SessionGroupServerConfig;

/**
 * 客户端认证请求
 *
 * @author zai 2020-04-07 10:57:11
 */
public class AuthReqHandler  {

	private SessionGroupServerConfig config;

	public AuthReqHandler(SessionGroupServerConfig config) {
		super();
		this.config = config;
	}

	@GGXAction
	public void handle(AuthReq req, GGXSession session) {
		if (this.config.getAuthToken().equals(req.getAuthToken())) {
			session.send(new AuthResp(true));
		}
	}

}
