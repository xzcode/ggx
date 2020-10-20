package com.ggx.registry.common.message.req;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 服务更新请求
 * 
 * @author zai
 * 2020-02-04 15:30:56
 */
public class RegistryServiceUpdateReq extends AbstractMessage{
	
	//服务信息
	private ServiceInfo serviceInfo;

	public RegistryServiceUpdateReq() {
		
	}

	public RegistryServiceUpdateReq(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	
}
