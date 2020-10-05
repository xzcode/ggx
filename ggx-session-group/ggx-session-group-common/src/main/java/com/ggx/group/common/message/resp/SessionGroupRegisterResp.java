package com.ggx.group.common.message.resp;

import com.ggx.core.common.message.model.AbstractMessage;

/**
 * 会话组注册响应
 *
 * @author zai
 * 2020-04-07 16:51:05
 */
public class SessionGroupRegisterResp extends AbstractMessage{
	
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
