package com.ggx.group.common.message.req;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.core.common.message.model.Message;
import com.ggx.group.common.constant.GGSesssionGroupConstant;

/**
 * 客户端认证请求
 *
 * @author zai
 * 2020-04-06 18:53:46
 */
public class AuthReq extends AbstractMessage {
	
	
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
