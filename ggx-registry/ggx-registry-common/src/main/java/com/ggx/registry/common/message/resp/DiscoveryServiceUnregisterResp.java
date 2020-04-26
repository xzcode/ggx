package com.ggx.registry.common.message.resp;

import com.ggx.core.common.message.model.Message;

/**
 * 服务下线通知
 * 
 * @author zai
 * 2020-02-04 11:07:41
 */
public class DiscoveryServiceUnregisterResp implements Message{
	
	public static final String ACTION = "GG.DISCOVERY.SERVICE.UNREGISTER.RESP";
	
	@Override
	public String getActionId() {
		return ACTION;
	}
	
	/**
	 * 服务名称
	 */
	private String serviceName;
	
	/**
	 * 服务id
	 */
	private String serviceId;
	
	

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	
}
