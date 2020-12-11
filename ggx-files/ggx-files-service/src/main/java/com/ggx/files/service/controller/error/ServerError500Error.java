package com.ggx.files.service.controller.error;

import com.ggx.spring.common.base.exception.LogicException;

@SuppressWarnings("serial")
public class ServerError500Error extends LogicException {
	
	public static final ServerError500Error INSTANCE = new ServerError500Error();

	public ServerError500Error() {
		super("服务器异常");
	}

}
