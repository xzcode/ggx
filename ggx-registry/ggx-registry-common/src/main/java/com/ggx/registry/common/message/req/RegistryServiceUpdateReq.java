package com.ggx.registry.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.constant.RegistryConstant;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 服务更新请求
 * 
 * @author zai
 * 2020-02-04 15:30:56
 */
public class RegistryServiceUpdateReq implements Message{
	
	public static final String ACTION_ID = RegistryConstant.ACTION_ID_PREFIX + ".SERVICE.UPDATE.REQ";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
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
