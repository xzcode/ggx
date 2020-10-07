package com.ggx.rpc.client.exception;

import com.ggx.util.exception.GGXNoStackTraceRuntimeException;

/**
 * RPC服务远端服务器失败异常
 * 
 * @author zai
 * 2020-10-6 15:12:35
 */
public class RpcServiceRemoteErrorException extends GGXNoStackTraceRuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	
	public RpcServiceRemoteErrorException(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String getMessage() {
		return String.format("Remote Service [%s] Error!", serviceName);
	}
	

}
