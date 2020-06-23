package com.ggx.admin.server.handler.registry.model.resp;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 注册中心信息模型
 *
 * @author zai
 * 2020-06-23 17:25:46
 */
public class ServiceInfoModel {
	
	//服务id
	protected String serviceId;
	
	//服务组id
	protected String serviceGroupId;
	
	//服务名称
	protected String serviceName;
	
	//服务ip地址
	protected String host;
	
	//所在地区
	protected String region = "default";
		
	//所在分区
	protected String zone = "default";
	
	//自定义数据
	protected Map<String, String> customData = new LinkedHashMap<>();
	
	
	/**
	 * 添加自定义参数
	 * 
	 * @param key
	 * @param value
	 * @author zai
	 * 2020-02-04 11:19:05
	 */
	public void addCustomData(String key, String value) {
		customData.put(key, value);
	}
	/**
	 * 添加给定集合中的所有参数
	 *
	 * @param customData
	 * @author zai
	 * 2020-06-23 17:39:13
	 */
	public void addAllCustomData(Map<String, String> customData) {
		this.customData.putAll(customData);
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
	
	
}
