package com.ggx.rpc.client.service.callback;

import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFuture;

/**
 * 方法调用信息
 * 
 * @author zai
 * 2020-10-4 14:02:34
 */
public class RpcMethodCallback {
	
	//调用编号
	private String rpcId;
	
	//超时时间长 毫秒ms
	private long timeout;
	
	//调用future
	private GGXDefaultFuture callbackFuture;
	
	private GGXFuture timeoutFuture;
	
	//是否异步
	private boolean async;
	
	private Class<?> returnType;
	
	public String getRpcId() {
		return rpcId;
	}

	public void setRpcId(String rpcId) {
		this.rpcId = rpcId;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public GGXDefaultFuture getCallbackFuture() {
		return callbackFuture;
	}

	public void setCallbackFuture(GGXDefaultFuture future) {
		this.callbackFuture = future;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}
	
	
	public GGXFuture getTimeoutFuture() {
		return timeoutFuture;
	}
	
	public void setTimeoutFuture(GGXFuture timeoutFuture) {
		this.timeoutFuture = timeoutFuture;
	}
	
	public Class<?> getReturnType() {
		return returnType;
	}
	
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	

}
