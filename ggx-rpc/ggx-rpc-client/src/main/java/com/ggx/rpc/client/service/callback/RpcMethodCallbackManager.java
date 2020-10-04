package com.ggx.rpc.client.service.callback;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.util.manager.impl.ListenableMapDataManager;

/**
 * RPC方法回调管理器
 * 
 * @author zai
 * 2020-10-4 14:06:58
 */
public class RpcMethodCallbackManager extends ListenableMapDataManager<String, RpcMethodCallback>{
	
	private TaskExecutor executor;
	
	protected RpcClientConfig config;

	public RpcMethodCallbackManager(RpcClientConfig config) {
		this.config = config;
		this.executor = config.getTaskExecutor();
	}
	
	@Override
	public RpcMethodCallback put(String key, RpcMethodCallback value) {
		long timeout = value.getTimeout();
		GGXFuture timeoutFuture = this.executor.schedule(timeout, () -> {
			GGXDefaultFuture callbackFuture = value.getCallbackFuture();
			callbackFuture.setSuccess(false);
			callbackFuture.setData(null);
			callbackFuture.setDone(true);
			if (!value.isAsync()) {
				value.notify();
			}
		});
		value.setTimeoutFuture(timeoutFuture);
		return super.put(key, value);
	}

}
