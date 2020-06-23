package com.ggx.admin.server.handler.registry;

import org.springframework.stereotype.Component;

import com.ggx.admin.server.handler.login.model.req.LoginReq;
import com.ggx.admin.server.handler.login.model.resp.LoginResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageDataHandler;
import com.ggx.core.spring.support.annotation.GGXMessageHandler;

/**
 * 登录控制器
 * 
 * 
 * @author zai
 * 2019-10-15 21:46:22
 */
@GGXMessageHandler(LoginReq.ACTION_ID)
public class ListenRegistryInfoHandler implements MessageDataHandler<LoginReq>{

	
	@Override
	public void handle(MessageData<LoginReq> request) {
		LoginReq req = request.getMessage();
		LoginResp resp = new LoginResp();
		
		System.out.println(req.getUsername());
		System.out.println(req.getPassword());
		
		resp.setCode(1);
		resp.setSuccess(true);
		request.getSession().send(LoginResp.ACTION, resp);
	}
	
}
