package com.ggx.rpc.client.service;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.service.group.RpcServiceGroup;
import com.ggx.rpc.client.service.provider.RpcServiceProvider;
import com.ggx.rpc.common.message.req.RpcReq;

public class RpcServiceManager {
	
	
	protected RpcClientConfig config;

	public RpcServiceManager(RpcClientConfig config) {
		this.config = config;
	}
	
	public RpcServiceProvider getServiceProvider() {
		return this.config.getServiceProvider();
	}
	
	public GGXFuture invoke(String serviceGroupId, RpcReq req) {
		RpcServiceGroup rpcServiceGroup = this.getServiceProvider().get(serviceGroupId);
		return rpcServiceGroup.invoke(req);
	}

	
}
