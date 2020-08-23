package com.ggx.registry.common.message.resp;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 服务更新推送
 * 
 * @author zai
 * 2020-02-04 11:34:37
 */
public class RegistryServiceUpdateResp  extends AbstractMessage{
	
	/**
	 * 服务信息
	 */
	private ServiceInfo serviceInfo;
	

	public RegistryServiceUpdateResp() {
		
	}

	public RegistryServiceUpdateResp(ServiceInfo serviceInfo) {
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
