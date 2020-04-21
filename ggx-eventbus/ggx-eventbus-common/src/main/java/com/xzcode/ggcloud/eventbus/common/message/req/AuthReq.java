package com.xzcode.ggcloud.eventbus.common.message.req;

import com.ggx.core.common.message.model.IMessage;
import com.xzcode.ggcloud.eventbus.common.constant.EventbusConstant;

/**
 * 客户端认证请求
 *
 * @author zai
 * 2020-04-06 18:53:46
 */
public class AuthReq implements IMessage {
	
	public static final String ACTION = EventbusConstant.ACTION_ID_PREFIX + "AUTH.REQ";
	
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
