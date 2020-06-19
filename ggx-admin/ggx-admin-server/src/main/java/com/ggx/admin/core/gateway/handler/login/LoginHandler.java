package com.ggx.admin.core.gateway.handler.login;

import org.springframework.stereotype.Component;

import com.ggx.admin.core.gateway.handler.login.model.req.LoginReq;
import com.ggx.admin.core.gateway.handler.login.model.resp.LoginResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageDataHandler;

/**
 * 登录控制器
 * 
 * 
 * @author zai
 * 2019-10-15 21:46:22
 */
@Component
public class LoginHandler implements MessageDataHandler<LoginReq>{

	
	@Override
	public void handle(MessageData<LoginReq> request) {
		LoginReq req = request.getMessage();
		LoginResp resp = new LoginResp();
		
		System.out.println(req.getToken());
		
		resp.setCode(1);
		resp.setSuccess(true);
		request.getSession().send(LoginResp.ACTION, resp);
	}
	
}
