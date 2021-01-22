package com.ggx.server.starter.config.sub;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.server.config.RpcServerConfig;

public class GGXServerRpcConfigSubModel {
	
	protected RpcServerConfig server;
	
	protected RpcClientConfig client;

	public RpcServerConfig getServer() {
		return server;
	}

	public void setServer(RpcServerConfig server) {
		this.server = server;
	}

	public RpcClientConfig getClient() {
		return client;
	}

	public void setClient(RpcClientConfig client) {
		this.client = client;
	}
	
	

}
