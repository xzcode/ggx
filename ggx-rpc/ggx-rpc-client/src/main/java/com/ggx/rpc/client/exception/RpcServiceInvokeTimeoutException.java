package com.ggx.rpc.client.exception;

import com.ggx.util.exception.GGXNoStackTraceRuntimeException;

/**
 * RPC服务调用超时异常
 * 
 * @author zai
 * 2020-10-6 15:12:35
 */
public class RpcServiceInvokeTimeoutException extends GGXNoStackTraceRuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	
	public RpcServiceInvokeTimeoutException(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String getMessage() {
		return String.format("Invoke Rpc Service [%s] Timeout!", serviceName);
	}
	

}
