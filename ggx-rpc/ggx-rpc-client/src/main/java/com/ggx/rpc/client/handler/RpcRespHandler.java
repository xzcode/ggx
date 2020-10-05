package com.ggx.rpc.client.handler;

import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.rpc.client.service.callback.RpcMethodCallback;
import com.ggx.rpc.client.service.callback.RpcMethodCallbackManager;
import com.ggx.rpc.common.message.resp.RpcResp;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.util.logger.GGXLogUtil;


public class RpcRespHandler implements MessageHandler<RpcResp>{
	
	protected RpcClientConfig config;
	protected RpcMethodCallbackManager rpcMethodCallbackManager;
	protected ParameterSerializerFactory parameterSerializerFactory;
	

	public RpcRespHandler(RpcClientConfig config) {
		this.config = config;
		this.rpcMethodCallbackManager = config.getRpcMethodCallbackManager();
		this.parameterSerializerFactory = config.getParameterSerializerFactory();
	}



	@Override
	public void handle(MessageData<RpcResp> messageData) {
		try {
			RpcResp resp = messageData.getMessage();
			String rpcId = resp.getRpcId();
			byte[] returnData = resp.getReturnData();
			RpcMethodCallback callback = this.rpcMethodCallbackManager.get(rpcId);
			if (callback == null) {
				return;
			}
			GGXFuture tof = callback.getTimeoutFuture();
			if (tof != null && tof.cancel()) {
				GGXDefaultFuture callbackFuture = callback.getCallbackFuture();
				callbackFuture.setSuccess(true);
				callbackFuture.setDone(true);
				callbackFuture.setData(this.parameterSerializerFactory.getSerializer(callback.getReturnType()).deserialize(returnData, callback.getReturnType()));
				if (!callback.isAsync()) {
					synchronized(callback) {
						if (callback.isWaiting()) {
							callback.notify();
						}
						callback.setNotified(true);
					}
				}
			}
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("RPC response ERROR!", e);
		}
		
	}

	

}
