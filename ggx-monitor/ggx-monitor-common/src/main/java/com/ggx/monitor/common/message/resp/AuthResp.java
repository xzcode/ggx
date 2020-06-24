package com.ggx.monitor.common.message.resp;

import com.ggx.core.common.message.model.Message;

/**
 * 认证响应
 *
 * @author zai
 * 2020-04-23 14:38:28
 */
public class AuthResp implements Message{
	
	public static final String ACTION = "GG.MONITOR.SERVICE.REGISTER.RESP";
	
	@Override
	public String getActionId() {
		return ACTION;
	}
	
	/**
	 * 是否认证成功
	 */
	private boolean success;
	
	/**
	 * 响应码
	 */
	private int code;
	
	/**
	 * 消息
	 */
	private String message;
	

	public AuthResp(boolean success) {
		super();
		this.success = success;
	}
	public AuthResp(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public AuthResp() {
		super();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
	
	
	
}
