package com.ggx.registry.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.handler.MessageHandler;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.common.message.resp.RegistryServiceUnregisterResp;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceUnregisterRespHandler implements MessageHandler<RegistryServiceUnregisterResp>{
	
	private RegistryClientConfig config;
	

	public ServiceUnregisterRespHandler(RegistryClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<RegistryServiceUnregisterResp> request) {
		RegistryServiceUnregisterResp resp = request.getMessage();
		String serviceGroupId = resp.getServiceGroupId();
		String serviceId = resp.getServiceId();
		ServiceManager serviceManager = config.getServiceManager();
		serviceManager.removeService(serviceGroupId, serviceId);
		
		GGXLogUtil.getLogger(this).warn("Service Unregister [{}:{}]!", resp.getServiceGroupId(), resp.getServiceId());
	}

	

}
