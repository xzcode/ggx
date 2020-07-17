package com.ggx.admin.server.handler.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.admin.common.collector.data.model.server.ServerData;
import com.ggx.admin.server.handler.registry.model.resp.ServerDataModel;
import com.ggx.admin.server.handler.registry.model.resp.ServiceDataModel;
import com.ggx.admin.server.handler.service.model.req.GetServiceInfoReq;
import com.ggx.admin.server.handler.service.model.resp.ListenServiceInfoResp;
import com.ggx.admin.server.listen.ServiceInfoSessionListenerManager;
import com.ggx.admin.server.model.SessionServiceListener;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.spring.support.annotation.GGXMessageHandler;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;

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
		
		serviceInfoSessionListenerManager.addOrUpdateListener(new SessionServiceListener(session, serviceId));

	}

}
