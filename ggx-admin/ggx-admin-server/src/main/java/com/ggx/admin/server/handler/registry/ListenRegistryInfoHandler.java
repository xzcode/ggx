package com.ggx.admin.server.handler.registry;

import com.ggx.admin.server.handler.login.model.req.LoginReq;
import com.ggx.admin.server.handler.login.model.resp.LoginResp;
import com.ggx.admin.server.handler.registry.model.req.ListenRegistryInfoReq;
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
@GGXMessageHandler(ListenRegistryInfoReq.ACTION_ID)
public class ListenRegistryInfoHandler implements MessageDataHandler<ListenRegistryInfoReq>{

	
	@Override
	public void handle(MessageData<ListenRegistryInfoReq> request) {
		
	}
	
}
