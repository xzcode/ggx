package com.ggx.registry.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.constant.RegistryConstant;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 客户端注册请求
 * 
 * 
 * @author zai
 * 2019-10-04 16:43:22
 */
public class RegistryServiceRegisterReq implements Message {
	
	public static final String ACTION_ID = RegistryConstant.ACTION_ID_PREFIX + ".SERVICE.REGISTER.REQ";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
	//认证token
	private String authToken;
	
	//服务信息
	private ServiceInfo serviceInfo;
	

	public RegistryServiceRegisterReq() {
	}

	public RegistryServiceRegisterReq(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
}
