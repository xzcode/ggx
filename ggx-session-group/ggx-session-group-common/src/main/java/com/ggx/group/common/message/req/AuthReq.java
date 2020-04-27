package com.ggx.group.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.group.common.constant.GGSesssionGroupConstant;

/**
 * 客户端认证请求
 *
 * @author zai
 * 2020-04-06 18:53:46
 */
public class AuthReq implements Message {
	
	public static final String ACTION = GGSesssionGroupConstant.ACTION_ID_PREFIX + "AUTH.REQ";
	
	@Override
	public String getActionId() {
		return ACTION;
	}
	
	//认证token
	private String authToken;
	

	public AuthReq() {
	}

	public AuthReq(String authToken) {
		this.authToken = authToken;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
}
