package com.ggx.registry.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.constant.RegistryConstant;

/**
 * 客户端注册响应
 * 
 * 
 * @author zai
 * 2019-10-04 16:44:38
 */
public class RegistryServiceRegisterResp implements Message{
	
	public static final String ACTION_ID = RegistryConstant.ACTION_ID_PREFIX + ".SERVICE.REGISTER.RESP";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
	/**
	 * 是否注册成功
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
	

	public RegistryServiceRegisterResp(boolean success) {
		super();
		this.success = success;
	}
	public RegistryServiceRegisterResp(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public RegistryServiceRegisterResp() {
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
