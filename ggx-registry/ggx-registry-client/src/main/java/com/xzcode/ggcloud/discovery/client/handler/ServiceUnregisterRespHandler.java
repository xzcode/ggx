package com.xzcode.ggcloud.discovery.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.xzcode.ggcloud.discovery.client.config.DiscoveryClientConfig;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceUnregisterResp;
import com.xzcode.ggcloud.discovery.common.service.ServiceManager;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceUnregisterRespHandler implements MessageDataHandler<DiscoveryServiceUnregisterResp>{
	
	private DiscoveryClientConfig config;
	

	public ServiceUnregisterRespHandler(DiscoveryClientConfig config) {
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
