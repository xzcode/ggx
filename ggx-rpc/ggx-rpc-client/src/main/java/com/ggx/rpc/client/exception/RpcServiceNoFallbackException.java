package com.ggx.rpc.client.exception;

import com.ggx.util.exception.GGXNoStackTraceRuntimeException;

/**
 * RPC服务未设置fallback异常
 * 
 * @author zai
 * 2020-10-6 15:12:35
 */
public class RpcServiceNoFallbackException extends GGXNoStackTraceRuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	private String methodName;
	
	public RpcServiceNoFallbackException(String serviceName, String methodName) {
		this.serviceName = serviceName;
		this.methodName = methodName;
	}

	@Override
	public String getMessage() {
		return String.format("Invoke method [%s] failed! Service [%s] does not have a fallback instance!", methodName, serviceName);
	}
	

}
