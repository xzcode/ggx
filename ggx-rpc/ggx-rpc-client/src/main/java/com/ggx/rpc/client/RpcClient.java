package com.ggx.rpc.client;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.proxy.RpcProxyManager;

public class RpcClient{
	
	private RpcClientConfig config;
	private RpcProxyManager proxyManager;
	
	public RpcClient(RpcClientConfig config) {
		this.config = config;
		this.config.setRpcClient(this);
		this.proxyManager = this.config.getProxyManager();
		init();
	}

	public void init() {
		
	}
	
	public void start() {
		
	}
	 
	public void register(Class<?> serviceInterface) {
		
		
	}
	

}
