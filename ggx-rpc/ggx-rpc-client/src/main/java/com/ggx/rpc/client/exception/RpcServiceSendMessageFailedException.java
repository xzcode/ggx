package com.ggx.rpc.client.exception;

import com.ggx.util.exception.GGXNoStackTraceRuntimeException;

/**
 * RPC服务消息发送失败异常
 * 
 * @author zai
 * 2020-10-6 15:12:35
 */
public class RpcServiceSendMessageFailedException extends GGXNoStackTraceRuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	
	public RpcServiceSendMessageFailedException(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String getMessage() {
		return String.format("Sending Message To Service [%s] Failed!", serviceName);
	}
	

}
