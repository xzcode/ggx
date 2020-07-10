package com.ggx.admin.server.handler.login.model.resp;

import com.ggx.core.common.message.model.AbstractMessage;

public class LoginResp extends AbstractMessage {

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
