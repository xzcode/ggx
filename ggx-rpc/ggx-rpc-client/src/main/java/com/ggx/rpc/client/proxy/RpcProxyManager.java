package com.ggx.rpc.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.util.manager.impl.ListenableMapDataManager;

public class RpcProxyManager extends ListenableMapDataManager<String, RpcProxyInfo>{
	
	private RpcClientConfig config;
	
	public RpcProxyManager(RpcClientConfig config) {
		this.config = config;
	}
	
	public void register(Class<?> serviceInterface) {
		
		InvocationHandler invocationHandler = this.config.getInvocationHandler();
		
		Object proxyObj = Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[] {serviceInterface}, invocationHandler);
		
		RpcProxyInfo proxyInfo = new RpcProxyInfo();
		proxyInfo.setName(serviceInterface.getCanonicalName());
		proxyInfo.setProxyInterfaceClazz(serviceInterface);
		proxyInfo.setProxyObj(proxyObj);
		
		this.put(proxyInfo.getName(), proxyInfo);
	}
	

}
