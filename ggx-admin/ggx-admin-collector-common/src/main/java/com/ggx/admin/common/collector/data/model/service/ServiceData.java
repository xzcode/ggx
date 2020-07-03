package com.ggx.admin.common.collector.data.model.service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 服务数据模型
 *
 * @author zai
 * 2020-06-24 14:49:42
 */
public class ServiceData {

	// 服务id
	protected String serviceId;

	// 服务组id
	protected String serviceGroupId;

	// 服务名称
	protected String serviceName;

	// 服务ip地址
	protected String host;

	// 自定义数据
	protected Map<String, String> customData = new LinkedHashMap<>();
	
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Map<String, String> getCustomData() {
		return customData;
	}

	public void setCustomData(Map<String, String> customData) {
		this.customData = customData;
	}
	
	
	

}
