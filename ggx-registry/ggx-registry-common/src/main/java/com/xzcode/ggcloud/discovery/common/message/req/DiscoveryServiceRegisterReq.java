package com.xzcode.ggcloud.discovery.common.message.req;

import com.ggx.core.common.message.model.IMessage;
import com.xzcode.ggcloud.discovery.common.message.req.model.ServiceInfoModel;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;

/**
 * 客户端注册请求
 * 
 * 
 * @author zai
 * 2019-10-04 16:43:22
 */
public class DiscoveryServiceRegisterReq implements IMessage {
	
	public static final String ACTION = "GG.DISCOVERY.SERVICE.REGISTER.REQ";
	
	@Override
	public String getActionId() {
		return ACTION;
	}
	
	//认证token
	private String authToken;
	
	//服务信息
	private ServiceInfo serviceInfo;
	

	public DiscoveryServiceRegisterReq() {
	}

	public DiscoveryServiceRegisterReq(ServiceInfo serviceInfo) {
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
