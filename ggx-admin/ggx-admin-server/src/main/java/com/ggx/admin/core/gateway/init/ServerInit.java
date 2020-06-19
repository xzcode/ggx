package com.ggx.admin.core.gateway.init;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ggx.admin.core.gateway.handler.login.LoginHandler;
import com.ggx.admin.core.gateway.handler.login.model.req.LoginReq;
import com.xzcode.ggserver.core.server.GGServer;

@Component
public class ServerInit {
	
	@Autowired
	private GGServer ggserver;
	
	@Autowired
	private LoginHandler loginHandler;
	
	@PostConstruct
	public void init() {
		
		ggserver.onMessage(LoginReq.ACTION_ID, loginHandler);
		
	}

}
