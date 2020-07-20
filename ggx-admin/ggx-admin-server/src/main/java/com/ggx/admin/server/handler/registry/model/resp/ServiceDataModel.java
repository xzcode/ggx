package com.ggx.admin.server.handler.registry.model.resp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ggx.registry.common.service.ServiceInfo;

public class ServiceDataModel {

	// 服务id
	protected String serviceId;

	// 服务组id
	protected String serviceGroupId;

	// 服务名称
	protected String serviceName;

	// 服务组说明名称
	protected String serviceGroupDescName;

	// 服务说明名称
	protected String serviceDescName;

	// 服务ip地址
	protected String host;

	// 服务端口
	protected int port;

	// 所在地区
	protected String region = "default";

	// 所在分区
	protected String zone = "default";

	// 自定义数据
	protected List<CustomData> customData = new ArrayList<>();

	public static ServiceDataModel create(ServiceInfo serviceInfo) {
		ServiceDataModel serviceModel = new ServiceDataModel();
		serviceModel.setServiceId(serviceInfo.getServiceId());
		serviceModel.setServiceName(serviceInfo.getServiceName());
		serviceModel.setServiceDescName(serviceInfo.getServiceDescName());
		serviceModel.setServiceGroupDescName(serviceInfo.getServiceGroupDescName());
		serviceModel.setHost(serviceInfo.getHost());
		serviceModel.setPort(serviceInfo.getPort());
		serviceModel.setRegion(serviceInfo.getRegion());
		serviceModel.setServiceGroupId(serviceInfo.getServiceGroupId());
		serviceModel.setZone(serviceInfo.getZone());
		Map<String, String> customData = serviceInfo.getCustomData();
		for (Entry<String, String> entry : customData.entrySet()) {
			serviceModel.addCustomData(entry.getKey(), entry.getValue());
		}
		return serviceModel;
	}

	/**
	 * 添加自定义参数
	 * 
	 * @param key
	 * @param value
	 * @author zai 2020-02-04 11:19:05
	 */
	public void addCustomData(String key, String value) {
		customData.add(new CustomData(key, value));
	}

	public String getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(String serviceName) {
		this.serviceGroupId = serviceName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String ip) {
		this.host = ip;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public List<CustomData> getCustomData() {
		return customData;
	}

	public void setCustomData(List<CustomData> customData) {
		this.customData = customData;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceGroupDescName() {
		return serviceGroupDescName;
	}

	public void setServiceGroupDescName(String serviceGroupDescName) {
		this.serviceGroupDescName = serviceGroupDescName;
	}

	public String getServiceDescName() {
		return serviceDescName;
	}

	public void setServiceDescName(String serviceDescName) {
		this.serviceDescName = serviceDescName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}