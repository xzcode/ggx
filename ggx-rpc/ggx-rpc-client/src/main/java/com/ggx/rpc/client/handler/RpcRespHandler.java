package com.ggx.rpc.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.common.message.resp.RpcResp;

/**
 * 事件发布响应
 *
 * @author zai
 * 2020-04-10 14:52:35
 */
public class RpcRespHandler implements MessageHandler<RpcResp>{
	
	protected RpcClientConfig config;
	

	public RpcRespHandler(RpcClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<RpcResp> request) {
	}

	

}
