package com.ggx.admin.common.collector.data.model.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ggx.admin.common.collector.data.model.server.ServerData;

/**
 * 服务数据模型
 *
 * @author zai 2020-06-24 14:49:42
 */
public class ServiceData {

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
	protected String region;

	// 所在分区
	protected String zone;
	
	
	protected ServerData serverData;

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
	
	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}
	
	public ServerData getServerData() {
		return serverData;
	}

}
