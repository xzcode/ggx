package com.ggx.rpc.client.proxy;

import java.lang.reflect.Proxy;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.invocation.ProxyInvocationHandler;
import com.ggx.rpc.common.cache.InterfaceInfo;
import com.ggx.rpc.common.cache.InterfaceInfoParser;
import com.ggx.util.manager.impl.ListenableMapDataManager;

public class RpcProxyManager extends ListenableMapDataManager<Class<?>, RpcProxyInfo>{
	
	private RpcClientConfig config;
	
	public RpcProxyManager(RpcClientConfig config) {
		this.config = config;
	}
	
	public Object register(Class<?> serviceInterface, Object fallbackObj) {
		
		
		InterfaceInfoParser interfaceInfoParser = this.config.getInterfaceInfoParser();
		InterfaceInfo interfaceInfo = interfaceInfoParser.parse(serviceInterface);
		
		Object proxyObj = Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[] {serviceInterface}, new ProxyInvocationHandler(config, serviceInterface, fallbackObj));
		
		RpcProxyInfo proxyInfo = new RpcProxyInfo();
		proxyInfo.setName(serviceInterface.getCanonicalName());
		proxyInfo.setProxyObj(proxyObj);
		proxyInfo.setInterfaceInfo(interfaceInfo);
		proxyInfo.setFallbackObj(fallbackObj);
		
		this.put(serviceInterface, proxyInfo);
		
		return proxyObj;
	}
	

}
