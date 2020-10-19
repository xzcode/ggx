package com.ggx.rpc.client.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.serializer.impl.KryoSerializer;
import com.ggx.core.common.session.GGXSession;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.exception.RpcServiceRemoteErrorException;
import com.ggx.rpc.client.service.callback.RpcMethodCallback;
import com.ggx.rpc.client.service.callback.RpcMethodCallbackManager;
import com.ggx.rpc.common.message.resp.RpcResp;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.util.logger.GGXLogUtil;


public class RpcRespHandler {
	
	protected RpcClientConfig config;
	protected RpcMethodCallbackManager rpcMethodCallbackManager;
	protected ParameterSerializerFactory parameterSerializerFactory;
	
	private Map<String, Class<?>> retuanDataTypeCache = new ConcurrentHashMap<>();
	

	public RpcRespHandler(RpcClientConfig config) {
		this.config = config;
		this.rpcMethodCallbackManager = config.getRpcMethodCallbackManager();
		this.parameterSerializerFactory = config.getParameterSerializerFactory();
	}



	@GGXAction
	public void handle(RpcResp resp, GGXSession session) {
		try {
			String rpcId = resp.getRpcId();
			byte[] returnData = resp.getReturnData();
			boolean success = resp.isSuccess();
			String returnDataType = resp.getReturnDataType();
			Class<?> returnDataClass = null;
			if (returnDataType != null) {
				returnDataClass = retuanDataTypeCache.get(returnDataType);
				if (returnDataClass == null) {
					returnDataClass = Class.forName(returnDataType);
					retuanDataTypeCache.put(returnDataType, returnDataClass);
				}
			}
			
			RpcMethodCallback callback = this.rpcMethodCallbackManager.get(rpcId);
			if (callback == null) {
				return;
			}
			callback.setReturnType(returnDataClass);
			GGXFuture tof = callback.getTimeoutFuture();
			if (tof != null && tof.cancel()) {
				GGXDefaultFuture callbackFuture = callback.getCallbackFuture();
				if (!success) {
					callback.setException(new RpcServiceRemoteErrorException(callback.getServiceName()));
				}
				if (callback.isAsync()) {
					if (returnDataClass != null) {
						callbackFuture.setData(this.parameterSerializerFactory.getDefaultSerializer().deserialize(returnData, returnDataClass));
					}
				}else {
					if (returnData != null) {
						callbackFuture.setData(this.parameterSerializerFactory.getDefaultSerializer().deserialize(returnData, callback.getReturnType()));
					}
					synchronized(callback) {
						if (callback.isWaiting()) {
							callback.notify();
						}
						callback.setNotified(true);
					}
				}
				callbackFuture.setSuccess(success);
				callbackFuture.setDone(true);
			}
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("RPC response ERROR!", e);
		}
		
	}

	

}
