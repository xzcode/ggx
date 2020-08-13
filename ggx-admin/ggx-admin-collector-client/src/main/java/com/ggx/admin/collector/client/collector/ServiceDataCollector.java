package com.ggx.admin.collector.client.collector;

import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.common.collector.data.collector.DataCollector;
import com.ggx.admin.common.collector.data.model.server.ServerData;
import com.ggx.admin.common.collector.data.model.service.ServiceData;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 服务信息数据收集器
 *
 * @author zai 2020-06-24 15:36:32
 */
public class ServiceDataCollector implements DataCollector<ServiceData> {

	protected GGXAdminCollectorClientConfig config;
	
	protected ServerDataCollector serverDataCollector;

	public ServiceDataCollector(GGXAdminCollectorClientConfig config) {
		this.config = config;
		serverDataCollector = new ServerDataCollector(config);

	}

	public ServiceData collect() {
		ServiceInfo cachedServiceInfo = this.config.getRegistryClient().getCachedServiceInfo();
		ServerData serverData = serverDataCollector.collect();
		if (cachedServiceInfo == null) {
			return null;
		}
		ServiceData serviceData = new ServiceData();
		serviceData.setServiceId(cachedServiceInfo.getServiceId());
		serviceData.setServiceGroupId(cachedServiceInfo.getServiceGroupId());
		serviceData.setServiceName(cachedServiceInfo.getServiceName());
		serviceData.setServiceDescName(cachedServiceInfo.getServiceDescName());
		serviceData.setServiceGroupDescName(cachedServiceInfo.getServiceGroupDescName());
		serviceData.setHost(cachedServiceInfo.getHost());
		serviceData.setPort(cachedServiceInfo.getPort());
		serviceData.setRegion(cachedServiceInfo.getRegion());
		serviceData.setZone(cachedServiceInfo.getZone());
		serviceData.setCustomData(cachedServiceInfo.getCustomData());

		serviceData.setServerData(serverData);
		
		return serviceData;
	}

}
