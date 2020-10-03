package com.ggx.rpc.client.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.ggx.rpc.client.RpcClient;
import com.ggx.rpc.client.config.RpcClientConfig;

public class ProxyInvocationHandler implements InvocationHandler {
	
	private RpcClientConfig config;
	private RpcClient rpcClient;
	

	public ProxyInvocationHandler(RpcClientConfig config) {
		this.config = config;
		this.rpcClient = this.config.getRpcClient();
	}


	

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		//TODO 确认需要调用的服务
		
		//TODO 组装数据包
		
		//TODO 发送数据包
		
		
		
		return null;
	}

}
