package com.ggx.registry.common.service;

import java.util.Map;
import java.util.TreeMap;

import com.ggx.core.common.session.GGXSession;

/**
 * 服务信息
 * 
 * 
 * @author zai 2019-10-04 16:14:45
 */
public class ServiceInfo {

	// 会话
	protected transient GGXSession session;

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
	protected Map<String, String> customData = new TreeMap<>();

	public ServiceInfo() {
		super();
	}

	/**
	 * 添加自定义参数
	 * 
	 * @param key
	 * @param value
	 * @author zai 2020-02-04 11:19:05
	 */
	public void addCustomData(String key, String value) {
		customData.put(key, value);
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

	public Map<String, String> getCustomData() {
		return customData;
	}

	public void setCustomData(Map<String, String> extraData) {
		this.customData = extraData;
	}

	public GGXSession getSession() {
		return session;
	}

	public void setSession(GGXSession session) {
		this.session = session;
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
