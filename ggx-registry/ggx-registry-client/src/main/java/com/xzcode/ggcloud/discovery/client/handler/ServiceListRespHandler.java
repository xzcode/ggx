package com.xzcode.ggcloud.discovery.client.handler;

import java.util.List;

import com.xzcode.ggcloud.discovery.client.config.DiscoveryClientConfig;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceListResp;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;
import com.xzcode.ggcloud.discovery.common.service.ServiceManager;
import com.xzcode.ggserver.core.common.message.MessageData;
import com.xzcode.ggserver.core.common.message.request.action.MessageDataHandler;

/**
 * 服务列表推送处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceListRespHandler implements MessageDataHandler<DiscoveryServiceListResp>{
	
	private DiscoveryClientConfig config;
	

	public ServiceListRespHandler(DiscoveryClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void handle(MessageData<DiscoveryServiceListResp> request) {
		
		DiscoveryServiceListResp resp = request.getMessage();
		//检查获取服务集合,内容属性存在null值问题
		List<ServiceInfo> serviceList = resp.getServiceList();
		
		if (serviceList == null) {
			return;
		}
		
		ServiceManager serviceManager = config.getServiceManager();
		
		for (ServiceInfo serviceInfo : serviceList) {
			serviceManager.registerService(serviceInfo);
		}
		
	}

	

}
