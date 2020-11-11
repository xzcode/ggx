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
		this.config.init();
	}
	
	public void init() {
		
	}

	
	@SuppressWarnings("unchecked")
	public <T> T register(Class<T> serviceInterface, Object fallbackObj) {
		return (T) this.proxyManager.register(serviceInterface, fallbackObj);
	}
	
	
	public <T> T getServiceImplements(Class<T> serviceInterface, Object fallbackObj) {
		return (T) this.proxyManager.register(serviceInterface, fallbackObj);
	}
	

	public void shutdown() {
		if (this.config.getSharedEventLoopGroup() != null) {
			this.config.getSharedEventLoopGroup().shutdownGracefully();
		}
		if (this.config.getTaskExecutor() != null && this.config.getTaskExecutor().getEventLoopGroup() != null) {
			this.config.getTaskExecutor().getEventLoopGroup().shutdownGracefully();
		}
	}

	

}
