package com.ggx.registry.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.constant.RegistryConstant;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 服务更新推送
 * 
 * @author zai
 * 2020-02-04 11:34:37
 */
public class RegistryServiceUpdateResp  implements Message{
	
	public static final String ACTION_ID = RegistryConstant.ACTION_ID_PREFIX + ".SERVICE.UPDATE.RESP";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
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
