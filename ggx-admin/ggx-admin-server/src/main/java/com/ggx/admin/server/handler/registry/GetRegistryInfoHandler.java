package com.ggx.admin.server.handler.registry;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.admin.server.handler.registry.model.req.GetRegistryInfoReq;
import com.ggx.admin.server.handler.registry.model.resp.GetRegistryInfoResp;
import com.ggx.admin.server.handler.registry.model.resp.ServiceDataModel;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.spring.support.annotation.GGXMessageHandler;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 登录控制器
 * 
 * 
 * @author zai 2019-10-15 21:46:22
 */
@GGXMessageHandler
public class GetRegistryInfoHandler implements MessageHandler<GetRegistryInfoReq> {

	@Autowired
	private RegistryClient registryClient;

	private ServiceManager serviceManager;

	@PostConstruct
	public void init() {
		this.serviceManager = registryClient.getConfig().getServiceManager();
	}

	@Override
	public void handle(MessageData<GetRegistryInfoReq> messageData) {
		GGSession session = messageData.getSession();
		List<ServiceInfo> serviceList = serviceManager.getServiceList();

		List<ServiceDataModel> serviceModels = new ArrayList<ServiceDataModel>(serviceList.size());
		for (ServiceInfo serviceInfo : serviceList) {
			ServiceDataModel serviceModel = ServiceDataModel.create(serviceInfo);
			serviceModels.add(serviceModel);
		}

		session.send(new GetRegistryInfoResp(serviceModels));

	}

}
