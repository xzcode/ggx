package com.ggx.rpc.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.handler.MessageHandler;
import com.ggx.core.common.session.GGXSession;
import com.ggx.rpc.common.message.req.RpcReq;
import com.ggx.rpc.common.message.resp.RpcResp;
import com.ggx.rpc.common.serializer.ParameterSerializer;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.rpc.server.invocation.InvocationManager;
import com.google.gson.Gson;


public class RpcReqHandler implements MessageHandler<RpcReq>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcReqHandler.class);
	
	private static final Gson GSON = new Gson();
	
	private RpcServerConfig config;

	private InvocationManager invocationManager;
	private ParameterSerializerFactory parameterSerializerFactory;
	

	public RpcReqHandler(RpcServerConfig config) {
		this.config = config;
		this.parameterSerializerFactory = config.getParameterSerializerFactory();
		this.invocationManager = config.getInvocationManager();
	}



	@Override
	public void handle(MessageData<RpcReq> messageData) {
		RpcReq req = messageData.getMessage();
		GGXSession session = messageData.getSession();
		GGXFuture future = this.invocationManager.invoke(req);
		String rpcId = req.getRpcId();
		future.addListener(f -> {
			if (f.isSuccess()) {
				//成功
				Object result = f.get();
				byte[] returnData = null;
				if (result != null) {
					ParameterSerializer<?> serializer = parameterSerializerFactory.getSerializer(result.getClass());
					returnData = serializer.serialize(result);
				}
				RpcResp rpcResp = new RpcResp(rpcId, returnData);
				session.send(rpcResp);
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("RPC Response, rpcId: {} , interface: {}, method: {}, return: {}", req.getRpcId(), req.getInterfaceName(), req.getMethodName(), GSON.toJson(result));
				}
				return;
			}
			//失败
			RpcResp rpcResp = new RpcResp(rpcId, false);
			session.send(rpcResp);
			
			LOGGER.warn("RPC Request FAILED!! rpcId: {} , interface: {}, method: {}", req.getRpcId(), req.getInterfaceName(), req.getMethodName());
			
		});
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("RPC Request, rpcId: {} , interface: {}, method: {}", req.getRpcId(), req.getInterfaceName(), req.getMethodName());
		}
		
	}

	

}
