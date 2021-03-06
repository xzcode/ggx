package com.ggx.group.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.group.common.constant.GGSesssionGroupConstant;

/**
 * 会话组注册响应
 *
 * @author zai
 * 2020-04-07 16:51:05
 */
public class SessionGroupRegisterResp implements Message{
	
	public static final String ACTION_ID = GGSesssionGroupConstant.ACTION_ID_PREFIX + "REGISTER.RESP";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
	// 是否成功
	private boolean success;


	public SessionGroupRegisterResp() {
		super();
	}


	public SessionGroupRegisterResp(boolean success) {
		super();
		this.success = success;
	}


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
	
	
}
