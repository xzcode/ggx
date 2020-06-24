package com.ggx.monitor.common.data.collector.impl;

import com.ggx.monitor.common.data.collector.DataCollector;
import com.ggx.monitor.common.data.model.service.ServiceData;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 服务信息数据收集器
 *
 * @author zai
 * 2020-06-24 15:36:32
 */
public class ServiceDataCollector implements DataCollector<ServiceData>{
	
	private RegistryClient registryClient;
	
	public ServiceDataCollector(RegistryClient registryClient) {
		super();
		this.registryClient = registryClient;
	}

	@Override
	public ServiceData collect() {
		ServiceInfo cachedServiceInfo = this.registryClient.getCachedServiceInfo();
		if (cachedServiceInfo == null) {
			return null;
		}
		ServiceData serviceData = new ServiceData();
		serviceData.setServiceId(cachedServiceInfo.getServiceId());
		serviceData.setServiceGroupId(cachedServiceInfo.getServiceGroupId());
		serviceData.setServiceName(cachedServiceInfo.getServiceName());
		serviceData.setHost(cachedServiceInfo.getHost());
		serviceData.setCustomData(cachedServiceInfo.getCustomData());
		
		return serviceData;
	}

	
}
