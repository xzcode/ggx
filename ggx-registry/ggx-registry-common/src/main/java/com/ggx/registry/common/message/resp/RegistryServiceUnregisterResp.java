package com.ggx.registry.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.constant.RegistryConstant;

/**
 * 服务下线通知
 * 
 * @author zai
 * 2020-02-04 11:07:41
 */
public class RegistryServiceUnregisterResp implements Message{
	
	public static final String ACTION_ID = RegistryConstant.ACTION_ID_PREFIX + ".SERVICE.UNREGISTER.RESP";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
	/**
	 * 服务组id
	 */
	private String serviceGroupId;
	
	/**
	 * 服务id
	 */
	private String serviceId;
	
	

	public String getServiceGroupId() {
		return serviceGroupId;
	}
	
	public void setServiceGroupId(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	
}
