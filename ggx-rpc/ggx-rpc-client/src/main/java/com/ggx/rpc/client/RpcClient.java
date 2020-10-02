package com.ggx.rpc.client;

import com.ggx.rpc.client.config.RpcClientConfig;

public class RpcClient{
	
	private RpcClientConfig config;
	
	public RpcClient(RpcClientConfig config) {
		this.config = config;
		this.config.setRpcClient(this);
		init();
	}

	public void init() {
		
	}
	
	public void start() {
		
	}
	

}
