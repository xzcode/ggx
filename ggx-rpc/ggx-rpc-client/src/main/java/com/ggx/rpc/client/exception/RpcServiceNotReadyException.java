package com.ggx.rpc.client.exception;

import com.ggx.util.exception.GGXNoStackTraceRuntimeException;

/**
 * RPC服务未就绪异常
 * 
 * @author zai
 * 2020-10-6 15:12:35
 */
public class RpcServiceNotReadyException extends GGXNoStackTraceRuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	
	public RpcServiceNotReadyException(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String getMessage() {
		return String.format("Service [%s] Not Ready!", serviceName);
	}
	

}
