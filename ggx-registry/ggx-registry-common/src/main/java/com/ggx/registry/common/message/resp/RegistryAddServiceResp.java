package com.ggx.registry.common.message.resp;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 新增服务推送
 * 
 * @author zai
 * 2020-02-10 19:43:35
 */
public class RegistryAddServiceResp extends AbstractMessage{
	
	/**
	 * 服务信息
	 */
	private ServiceInfo serviceInfo;
	

	public RegistryAddServiceResp() {
		
	}

	public RegistryAddServiceResp(ServiceInfo serviceInfo) {
		super();
		this.serviceInfo = serviceInfo;
	}


	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}
	
	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	
}
