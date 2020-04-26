package com.ggx.registry.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.common.message.resp.DiscoveryServiceUnregisterResp;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceUnregisterRespHandler implements MessageDataHandler<DiscoveryServiceUnregisterResp>{
	
	private RegistryClientConfig config;
	

	public ServiceUnregisterRespHandler(RegistryClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<DiscoveryServiceUnregisterResp> request) {
		DiscoveryServiceUnregisterResp resp = request.getMessage();
		String serviceName = resp.getServiceName();
		String serviceId = resp.getServiceId();
		ServiceManager serviceManager = config.getServiceManager();
		serviceManager.removeService(serviceName, serviceId);
	}

	

}
