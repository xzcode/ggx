package com.ggx.admin.common.collector.message.req;

import com.ggx.core.common.message.model.AbstractMessage;

/**
 * 客户端注册请求
 * 
 * 
 * @author zai
 * 2019-10-04 16:43:22
 */
public class AuthReq extends AbstractMessage {
	
	
	//认证token
	private String authToken;
	
	
	private String serviceId;
	

	public AuthReq() {
	}

	public void setAuthToken(String authToken, String serviceId) {
		this.authToken = authToken;
		this.serviceId = serviceId;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
}
