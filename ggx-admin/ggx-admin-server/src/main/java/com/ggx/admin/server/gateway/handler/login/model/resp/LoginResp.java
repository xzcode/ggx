package com.ggx.admin.server.gateway.handler.login.model.resp;

public class LoginResp {
	
	public static final String ACTION = "login.resp";
	
	private boolean success;
	
	private int code;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	
	
	
	
	
}
