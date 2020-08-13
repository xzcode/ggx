package com.ggx.admin.server.handler.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.admin.server.handler.service.model.req.GetServiceInfoReq;
import com.ggx.admin.server.listener.service.ServiceInfoSessionListenerManager;
import com.ggx.admin.server.model.ServiceInfoSessionListener;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.spring.support.annotation.GGXMessageHandler;

/**
 * 监听服务信息处理器
 *
 * @author zai
 * 2020-07-17 16:32:11
 */
@GGXMessageHandler
public class ListenServiceInfoHandler implements MessageHandler<GetServiceInfoReq> {

	@Autowired
	private ServiceInfoSessionListenerManager serviceInfoSessionListenerManager;
	

	@Override
	public void handle(MessageData<GetServiceInfoReq> messageData) {
		GetServiceInfoReq req = messageData.getMessage();
		
		GGSession session = messageData.getSession();
		String serviceId = req.getServiceId();
		
		serviceInfoSessionListenerManager.addOrUpdateListener(new ServiceInfoSessionListener(session, serviceId));

	}

}
