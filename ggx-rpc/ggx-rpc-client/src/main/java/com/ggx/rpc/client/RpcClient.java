package com.ggx.rpc.client;

import java.util.List;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.rpc.client.service.RpcService;
import com.ggx.rpc.client.service.group.RpcServiceGroup;

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
	
	
	@SuppressWarnings("unchecked")
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
	
	public List<RpcServiceGroup> getAllRpcServiceGroup() {
		return this.config.getServiceManager().getList();
	}

	

}
