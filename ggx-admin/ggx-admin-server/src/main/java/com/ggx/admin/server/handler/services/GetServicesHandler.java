package com.ggx.admin.server.handler.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.admin.server.handler.services.model.req.GetServicesReq;
import com.ggx.admin.server.handler.services.model.resp.GetServicesResp;
import com.ggx.admin.server.handler.services.model.resp.ServiceModel;
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
 * @author zai
 * 2019-10-15 21:46:22
 */
@GGXMessageHandler
public class GetServicesHandler implements MessageHandler<GetServicesReq>{
	
	
	@Autowired
	private RegistryClient registryClient;
	
	private ServiceManager serviceManager;
	
	@PostConstruct
	public void init() {
		this.serviceManager = registryClient.getConfig().getServiceManager();
	}
	
	
	@Override
	public void handle(MessageData<GetServicesReq> messageData) {
		GGSession session = messageData.getSession();
		List<ServiceInfo> serviceList = serviceManager.getServiceList();
		
		List<ServiceModel> serviceModels = new ArrayList<ServiceModel>(serviceList.size());
		for (ServiceInfo serviceInfo : serviceList) {
			ServiceModel serviceModel = new ServiceModel();
			serviceModel.setServiceId(serviceInfo.getServiceId());
			serviceModel.setServiceName(serviceInfo.getServiceName());
			serviceModel.setHost(serviceInfo.getHost());
			serviceModel.setRegion(serviceInfo.getRegion());
			serviceModel.setServiceGroupId(serviceInfo.getServiceGroupId());
			serviceModel.setZone(serviceInfo.getZone());
			Map<String, String> customData = serviceInfo.getCustomData();
			for (Entry<String, String> entry : customData.entrySet()) {
				serviceModel.addCustomData(entry.getKey(), entry.getValue());
			}
			serviceModels.add(serviceModel );
		}
		
		session.send(new GetServicesResp(serviceModels));
		
	}
	
}
