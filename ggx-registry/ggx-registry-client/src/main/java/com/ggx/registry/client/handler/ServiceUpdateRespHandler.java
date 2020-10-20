package com.ggx.registry.client.handler;

import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.common.message.resp.RegistryServiceUpdateResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 服务更新推送处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceUpdateRespHandler {
	
	private RegistryClientConfig config;
	private ServiceManager serviceManager;
	

	public ServiceUpdateRespHandler(RegistryClientConfig config) {
		super();
		this.config = config;
		this.serviceManager = this.config.getServiceManager();
	}


	@GGXAction
	public void handle(RegistryServiceUpdateResp resp) {
		ServiceInfo updateModel = resp.getServiceInfo();
		if (updateModel == null) {
			return;
		}
		serviceManager.updateService(updateModel);
		
	}

	

}
