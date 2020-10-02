package com.ggx.rpc.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.rpc.common.message.req.RpcReq;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.google.gson.Gson;

/**
 * 事件发布请求
 *
 * @author zai
 * 2020-04-10 14:49:48
 */
public class RpcReqHandler implements MessageHandler<RpcReq>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcReqHandler.class);
	
	private static final Gson GSON = new Gson();
	
	private RpcServerConfig config;
	

	public RpcReqHandler(RpcServerConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<RpcReq> messageData) {
		RpcReq req = messageData.getMessage();
		
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\nPublish Event ['{}'] ", GSON.toJson(req));
		}
		
	}

	

}
