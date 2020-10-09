package com.ggx.dashboard.server.handler.login;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.dashboard.server.handler.login.model.req.LoginReq;
import com.ggx.dashboard.server.handler.login.model.resp.LoginResp;
import com.ggx.server.spring.boot.starter.annotation.GGXController;

/**
 * 登录控制器
 * 
 * 
 * @author zai 2019-10-15 21:46:22
 */
@GGXController
public class LoginHandler{

	@GGXAction
	public void handle(LoginReq req, GGXSession session) {
		LoginResp resp = new LoginResp();

		resp.setCode(1);
		resp.setSuccess(true);
		session.getSession().send(resp);
	}

}
