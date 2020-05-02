package com.ggx.registry.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.constant.RegistryConstant;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 新增服务推送
 * 
 * @author zai
 * 2020-02-10 19:43:35
 */
public class RegistryAddServiceResp  implements Message{
	
	public static final String ACTION_ID = RegistryConstant.ACTION_ID_PREFIX + ".ADD.SERVICE.RESP";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
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
