package com.ggx.monitor.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.monitor.common.constant.GameMonitorConstant;

/**
 * 客户端注册请求
 * 
 * 
 * @author zai
 * 2019-10-04 16:43:22
 */
public class AuthReq implements Message {
	
	public static final String ACTION_ID = GameMonitorConstant.ACTION_ID_PREFIX + "AUTH.REQ";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
	//认证token
	private String authToken;
	

	public AuthReq() {
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
}
