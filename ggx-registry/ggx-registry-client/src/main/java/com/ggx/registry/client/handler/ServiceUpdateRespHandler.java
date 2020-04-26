package com.ggx.registry.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.common.message.req.model.ServiceInfoModel;
import com.ggx.registry.common.message.resp.DiscoveryServiceUpdateResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 服务更新推送处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceUpdateRespHandler implements MessageDataHandler<DiscoveryServiceUpdateResp>{
	
	private RegistryClientConfig config;
	

	public ServiceUpdateRespHandler(RegistryClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void handle(MessageData<DiscoveryServiceUpdateResp> request) {
		
		DiscoveryServiceUpdateResp resp = request.getMessage();
		ServiceInfo updateModel = resp.getServiceInfo();
		if (updateModel == null) {
			return;
		}
		
		ServiceManager serviceManager = config.getServiceManager();
		ServiceInfo service = serviceManager.getService(updateModel.getServiceId());
		if (service != null) {
			service.getCustomData().putAll(updateModel.getCustomData());
			serviceManager.updateService(service);
		}
		
	}

	

}
