package com.ggx.rpc.client.proxy;

import java.lang.reflect.Proxy;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.invocation.handler.DefaultProxyInvocationHandler;
import com.ggx.rpc.common.Interfaceinfo.InterfaceInfo;
import com.ggx.rpc.common.Interfaceinfo.InterfaceInfoParser;
import com.ggx.util.manager.map.impl.ListenableMapDataManager;

public class RpcProxyManager extends ListenableMapDataManager<Class<?>, RpcProxyInfo>{
	
	private RpcClientConfig config;
	
	public RpcProxyManager(RpcClientConfig config) {
		this.config = config;
	}
	
	public Object register(Class<?> serviceInterface, Object fallbackObj) {
		
		
		InterfaceInfoParser interfaceInfoParser = this.config.getInterfaceInfoParser();
		InterfaceInfo interfaceInfo = interfaceInfoParser.parse(serviceInterface);
		
		Object proxyObj = Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[] {serviceInterface}, new DefaultProxyInvocationHandler(config, serviceInterface));
		
		RpcProxyInfo proxyInfo = new RpcProxyInfo();
		proxyInfo.setName(serviceInterface.getCanonicalName());
		proxyInfo.setProxyObj(proxyObj);
		proxyInfo.setInterfaceInfo(interfaceInfo);
		proxyInfo.setFallbackObj(fallbackObj);
		
		this.put(serviceInterface, proxyInfo);
		
		return proxyObj;
	}
	
	public Object register(Class<?> serviceInterface, Object proxyObj, Object fallbackObj) {
		
		InterfaceInfoParser interfaceInfoParser = this.config.getInterfaceInfoParser();
		InterfaceInfo interfaceInfo = interfaceInfoParser.parse(serviceInterface);
		
		RpcProxyInfo proxyInfo = new RpcProxyInfo();
		proxyInfo.setName(serviceInterface.getCanonicalName());
		proxyInfo.setProxyObj(proxyObj);
		proxyInfo.setInterfaceInfo(interfaceInfo);
		proxyInfo.setFallbackObj(fallbackObj);
		
		this.put(serviceInterface, proxyInfo);
		
		return proxyObj;
	}
	

}
