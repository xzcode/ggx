package com.ggx.registry.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.constant.RegistryConstant;
import com.ggx.registry.common.service.ServiceInfo;

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
	
	/**
	 * 注册后的服务信息
	 */
	private ServiceInfo serviceInfo;
	
	

	public RegistryServiceRegisterResp(ServiceInfo serviceInfo, boolean success) {
		this.success = success;
		this.serviceInfo = serviceInfo;
	}
	public RegistryServiceRegisterResp(ServiceInfo serviceInfo, boolean success, String message) {
		this.serviceInfo = serviceInfo;
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
	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}
	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	
	
	
	
	
}
