package com.ggx.admin.core.gateway.handler.login.model.req;

public class LoginReq {

	public static final String ACTION_ID = "login.req";

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
