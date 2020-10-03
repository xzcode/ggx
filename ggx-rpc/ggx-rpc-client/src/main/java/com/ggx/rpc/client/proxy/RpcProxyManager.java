package com.ggx.rpc.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.common.cache.InterfaceInfo;
import com.ggx.rpc.common.cache.InterfaceInfoParser;
import com.ggx.util.manager.impl.ListenableMapDataManager;

public class RpcProxyManager extends ListenableMapDataManager<String, RpcProxyInfo>{
	
	private RpcClientConfig config;
	
	public RpcProxyManager(RpcClientConfig config) {
		this.config = config;
	}
	
	public void register(Class<?> serviceInterface, Object fallbackObj) {
		
		InvocationHandler invocationHandler = this.config.getInvocationHandler();
		
		InterfaceInfoParser interfaceInfoParser = this.config.getInterfaceInfoParser();
		InterfaceInfo interfaceInfo = interfaceInfoParser.parse(serviceInterface);
		
		Object proxyObj = Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[] {serviceInterface}, invocationHandler);
		
		RpcProxyInfo proxyInfo = new RpcProxyInfo();
		proxyInfo.setName(serviceInterface.getCanonicalName());
		proxyInfo.setProxyObj(proxyObj);
		proxyInfo.setInterfaceInfo(interfaceInfo);
		proxyInfo.setFallbackObj(fallbackObj);
		
		this.put(interfaceInfo.getInterfaceName(), proxyInfo);
	}
	

}
