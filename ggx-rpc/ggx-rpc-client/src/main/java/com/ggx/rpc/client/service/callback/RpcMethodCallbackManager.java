package com.ggx.rpc.client.service.callback;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.exception.RpcServiceInvokeTimeoutException;
import com.ggx.util.manager.impl.ListenableMapDataManager;

/**
 * RPC方法回调管理器
 * 
 * @author zai
 * 2020-10-4 14:06:58
 */
public class RpcMethodCallbackManager extends ListenableMapDataManager<String, RpcMethodCallback>{
	
	
	protected RpcClientConfig config;

	public RpcMethodCallbackManager(RpcClientConfig config) {
		this.config = config;
	}
	
	@Override
	public RpcMethodCallback put(String key, RpcMethodCallback callback) {
		TaskExecutor taskExecutor = config.getTaskExecutor();
		long timeout = callback.getTimeout();
		GGXFuture timeoutFuture = taskExecutor.schedule(timeout, () -> {
				GGXDefaultFuture callbackFuture = callback.getCallbackFuture();
				RpcServiceInvokeTimeoutException timeoutException = new RpcServiceInvokeTimeoutException(callback.getServiceName());
				callback.setException(timeoutException);
				callbackFuture.setSuccess(false);
				callbackFuture.setData(null);
				callbackFuture.setDone(true);
				callbackFuture.setCause(timeoutException);
				if (!callback.isAsync()) {
					synchronized(callback) {
						if (callback.isWaiting()) {
							callback.notify();
						}
						callback.setNotified(true);
					}
				}
		});
		callback.setTimeoutFuture(timeoutFuture);
		return super.put(key, callback);
	}

}
