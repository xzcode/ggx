package com.ggx.rpc.client;

import com.ggx.core.common.executor.DefaultTaskExecutor;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.proxy.RpcProxyManager;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;

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
		EventLoopGroup sharedEventLoopGroup = this.config.getSharedEventLoopGroup();
		if (sharedEventLoopGroup == null) {
			this.config.setSharedEventLoopGroup(new DefaultEventLoopGroup(this.config.getWorkThreadSize(), new GGXThreadFactory("ggx-rpc-cli-", false)));
		}
		
		if (this.config.getTaskExecutor() == null) {
			this.config.setTaskExecutor(new DefaultTaskExecutor(sharedEventLoopGroup));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T register(Class<T> serviceInterface, Object fallbackObj) {
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
