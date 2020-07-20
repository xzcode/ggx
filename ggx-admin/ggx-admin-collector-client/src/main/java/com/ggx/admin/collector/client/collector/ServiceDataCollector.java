package com.ggx.admin.collector.client.collector;

import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.common.collector.data.collector.DataCollector;
import com.ggx.admin.common.collector.data.model.service.ServiceData;
import com.ggx.admin.common.collector.message.req.ServiceDataReq;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 服务信息数据收集器
 *
 * @author zai 2020-06-24 15:36:32
 */
public class ServiceDataCollector implements DataCollector<ServiceData> {

	protected GGXAdminCollectorClientConfig config;

	public ServiceDataCollector(GGXAdminCollectorClientConfig config) {
		this.config = config;

	}

	@Override
	public long collectPeriodMs() {
		return 5000L;
	}

	public ServiceData collect() {
		ServiceInfo cachedServiceInfo = this.config.getRegistryClient().getCachedServiceInfo();
		if (cachedServiceInfo == null) {
			return null;
		}
		ServiceData serviceData = new ServiceData();
		serviceData.setServiceId(cachedServiceInfo.getServiceId());
		serviceData.setServiceGroupId(cachedServiceInfo.getServiceGroupId());
		serviceData.setServiceName(cachedServiceInfo.getServiceName());
		serviceData.setHost(cachedServiceInfo.getHost());
		serviceData.setCustomData(cachedServiceInfo.getCustomData());
		config.getSession().send(new ServiceDataReq(serviceData));

		return serviceData;
	}

}
