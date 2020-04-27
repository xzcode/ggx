package com.ggx.group.server.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.group.common.message.req.AuthReq;
import com.ggx.group.common.message.resp.AuthResp;
import com.ggx.group.server.config.SessionGroupServerConfig;

/**
 * 客户端认证请求
 *
 * @author zai 2020-04-07 10:57:11
 */
public class AuthReqHandler implements MessageDataHandler<AuthReq> {

	private SessionGroupServerConfig config;

	public AuthReqHandler(SessionGroupServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void handle(MessageData<AuthReq> request) {
		GGSession session = request.getSession();
		AuthReq req = request.getMessage();
		if (this.config.getAuthToken().equals(req.getAuthToken())) {
			session.send(new AuthResp(true));
		}
	}

}
