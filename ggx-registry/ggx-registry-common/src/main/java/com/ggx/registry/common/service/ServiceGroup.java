package com.ggx.registry.common.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务信息组
 * 
 * 
 * @author zai
 * 2019-10-04 16:14:45
 */
public class ServiceGroup {
	
	//服务组id
	protected String serviceGroupId;
	
	protected Map<String, ServiceInfo> services = new ConcurrentHashMap<>(100);
	
	
	public ServiceGroup(String serviceGroupId) {
		super();
		this.serviceGroupId = serviceGroupId;
	}

	public ServiceInfo addServiceInfo(ServiceInfo serviceInfo) {
		return services.put(serviceInfo.getServiceId(), serviceInfo);
	}
	
	public ServiceInfo removeServiceInfo(String serviceId) {
		return services.remove(serviceId);
	}
	public boolean removeServiceInfo(ServiceInfo serviceInfo) {
		return services.remove(serviceInfo.getServiceId(),serviceInfo);
	}
	
	public ServiceInfo getServiceInfo(String serviceId) {
		return services.get(serviceId);
	}
	
	public Map<String, ServiceInfo> getServices() {
		return services;
	}
	
	public List<ServiceInfo> getServiceList() {
		List<ServiceInfo> list = new ArrayList<>();
		Iterator<Entry<String, ServiceInfo>> serviceIterator = services.entrySet().iterator();
		while (serviceIterator.hasNext()) {
			ServiceInfo serviceInfo = (ServiceInfo) serviceIterator.next().getValue();
			list.add(serviceInfo);
		}
		return list;
	}
	
	public String getServiceGroupId() {
		return serviceGroupId;
	}
	
	public void setServiceGroupId(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}
	
	public void setServices(Map<String, ServiceInfo> services) {
		this.services = services;
	}
	
	
	
}
