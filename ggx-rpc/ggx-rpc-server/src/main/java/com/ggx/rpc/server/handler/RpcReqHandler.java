package com.ggx.rpc.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.rpc.common.message.req.RpcReq;
import com.ggx.rpc.common.message.resp.RpcResp;
import com.ggx.rpc.common.serializer.ParameterSerializer;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.rpc.server.invocation.InvocationManager;
import com.google.gson.Gson;

public class RpcReqHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcReqHandler.class);

	private static final Gson GSON = new Gson();

	private RpcServerConfig config;

	private InvocationManager invocationManager;
	private ParameterSerializerFactory parameterSerializerFactory;

	public RpcReqHandler(RpcServerConfig config) {
		this.config = config;
		this.parameterSerializerFactory = this.config.getParameterSerializerFactory();
		this.invocationManager = this.config.getInvocationManager();
	}

	@GGXAction
	public void handle(RpcReq req, GGXSession session) {

		Runnable invokeTask = new Runnable() {

			@Override
			public void run() {

				GGXFuture<?> future = RpcReqHandler.this.invocationManager.invoke(req);
				String rpcId = req.getRpcId();
				future.addListener(f -> {
					
					Object result = f.get();
					
					if (f.isSuccess()) {
						// 成功
						byte[] returnData = null;
						String returnDataType = null;
						if (result != null) {
							ParameterSerializer<?> serializer = parameterSerializerFactory
									.getSerializer(result.getClass());
							returnData = serializer.serialize(result);
							returnDataType = result.getClass().getName();
						}
						RpcResp rpcResp = new RpcResp(rpcId, returnData, returnDataType);
						session.send(rpcResp);
						
						return;
					}else {
						// 失败
						RpcResp rpcResp = new RpcResp(rpcId, false);
						session.send(rpcResp);
					}
					
					if (LOGGER.isDebugEnabled()) {
						LOGGER.info("RPC Response, rpcId: {} , interface: {}, method: {}, return: {}",
								req.getRpcId(), req.getInterfaceName(), req.getMethodName(), result != null ? GSON.toJson(result) : null);
					}
				});
				

			}
		};
		
		//是否使用自定义线程池
		if (this.config.isUseCustomRpcThreadPool()) {
			this.config.getCustomRpcThreadPool().submit(invokeTask);
			return;
		}
		invokeTask.run();
		
	}

}
