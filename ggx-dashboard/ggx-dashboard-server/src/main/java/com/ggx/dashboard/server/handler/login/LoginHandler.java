package com.ggx.dashboard.server.handler.login;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.spring.support.annotation.GGXMessageHandler;
import com.ggx.dashboard.server.handler.login.model.req.LoginReq;
import com.ggx.dashboard.server.handler.login.model.resp.LoginResp;

/**
 * 登录控制器
 * 
 * 
 * @author zai 2019-10-15 21:46:22
 */
@GGXMessageHandler
public class LoginHandler implements MessageHandler<LoginReq> {

	@Override
	public void handle(MessageData<LoginReq> request) {
		LoginReq req = request.getMessage();
		LoginResp resp = new LoginResp();

		System.out.println(req.getUsername());
		System.out.println(req.getPassword());

		resp.setCode(1);
		resp.setSuccess(true);
		request.getSession().send(resp);
	}

}
