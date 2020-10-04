package com.ggx.rpc.client.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.ggx.rpc.client.RpcClient;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.proxy.RpcProxyInfo;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.rpc.client.service.InterfaceServiceGroupCache;
import com.ggx.rpc.common.cache.InterfaceInfo;
import com.ggx.rpc.common.cache.InterfaceInfoParser;

public class ProxyInvocationHandler implements InvocationHandler {
	
	private RpcClientConfig config;
	private Class<?> serviceInterface;
	private Object fallbackObj;
	private RpcClient rpcClient;
	private InterfaceServiceGroupCache interfaceServiceGroupCache;
	private RpcProxyManager proxyManager;
	private InterfaceInfoParser interfaceInfoParser;
	

	public ProxyInvocationHandler(RpcClientConfig config, Class<?> serviceInterface, Object fallbackObj) {
		this.config = config;
		this.serviceInterface = serviceInterface;
		this.fallbackObj = fallbackObj;
		this.rpcClient = this.config.getRpcClient();
		this.interfaceServiceGroupCache = this.config.getInterfaceServiceGroupCache();
		this.proxyManager = this.config.getProxyManager();
		this.interfaceInfoParser = this.config.getInterfaceInfoParser();
	}

	@Override
	public Object invoke(Object proxy, Method proxyMethod, Object[] args) throws Throwable {
		
		//TODO 确认需要调用的服务
		RpcProxyInfo proxyInfo = proxyManager.get(serviceInterface);
		InterfaceInfo interfaceInfo = proxyInfo.getInterfaceInfo();
		Map<String, Method> methods = interfaceInfo.getMethods();
		Method method = methods.get(interfaceInfoParser.makeMethodKey(proxyMethod, proxyMethod.getParameterTypes()));
		
		if (method == null) {
			return proxyMethod.invoke(this.fallbackObj, args);
		}
		
		Class<?>[] paramTypes = proxyInfo.getInterfaceInfo().getMethodParamTypes().get(method);
		
		//TODO 组装数据包
		
		//TODO 发送数据包
		
		
		
		return null;
	}

}
