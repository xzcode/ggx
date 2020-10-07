package com.ggx.registry.client.handler;

import java.util.List;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.handler.MessageHandler;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.common.message.resp.RegistryServiceListResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 服务列表推送处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceListRespHandler implements MessageHandler<RegistryServiceListResp>{
	
	private RegistryClientConfig config;
	

	public ServiceListRespHandler(RegistryClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void handle(MessageData<RegistryServiceListResp> request) {
		
		RegistryServiceListResp resp = request.getMessage();
		//检查获取服务集合,内容属性存在null值问题
		List<ServiceInfo> serviceList = resp.getServiceList();
		
		if (serviceList == null) {
			return;
		}
		
		ServiceManager serviceManager = config.getServiceManager();
		
		List<ServiceInfo> oldServiceList = serviceManager.getServiceList();
		
		for (ServiceInfo oldServiceInfo : oldServiceList) {
			boolean hasServiceInfo = false;
			for (ServiceInfo newServiceInfo : serviceList) {
				if (newServiceInfo.getServiceId().equals(oldServiceInfo.getServiceId())) {
					hasServiceInfo = true;
					break;
				}
			}
			if (!hasServiceInfo) {
				serviceManager.removeService(oldServiceInfo);
			}
		}
		
		for (ServiceInfo serviceInfo : serviceList) {
			serviceManager.registerService(serviceInfo);
		}
		
	}

	

}
