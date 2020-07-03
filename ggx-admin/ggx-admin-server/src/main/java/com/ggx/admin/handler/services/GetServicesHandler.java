package com.ggx.admin.handler.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.admin.collector.server.service.ServiceDataService;
import com.ggx.admin.handler.services.model.req.GetServicesReq;
import com.ggx.admin.handler.services.model.resp.GetServicesResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.spring.support.annotation.GGXMessageHandler;

/**
 * 登录控制器
 * 
 * 
 * @author zai
 * 2019-10-15 21:46:22
 */
@GGXMessageHandler
public class GetServicesHandler implements MessageHandler<GetServicesReq>{
	
	@Autowired
	private ServiceDataService serviceDataService;

	
	@Override
	public void handle(MessageData<GetServicesReq> messageData) {
		GGSession session = messageData.getSession();
		session.send(new GetServicesResp(serviceDataService.getDataList()));
		
	}
	
}
