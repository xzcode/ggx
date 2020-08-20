package com.ggx.admin.server.handler.login.model.req;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.docs.core.annotation.DocsModelProperty;

public class LoginReq extends AbstractMessage {


	@DocsModelProperty("用户名")
	private String username;

	@DocsModelProperty("密码")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
