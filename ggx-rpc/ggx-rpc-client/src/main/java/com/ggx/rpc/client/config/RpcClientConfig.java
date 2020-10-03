package com.ggx.rpc.client.config;

import java.lang.reflect.InvocationHandler;

import com.ggx.registry.client.RegistryClient;
import com.ggx.rpc.client.RpcClient;
import com.ggx.rpc.client.invocation.ProxyInvocationHandler;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.rpc.client.service.RpcServiceManager;
import com.ggx.rpc.client.service.provider.RpcServiceProvider;
import com.ggx.rpc.common.cache.InterfaceInfoParser;
import com.ggx.rpc.common.constant.RpcConstant;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.rpc.common.serializer.factory.impl.DefaultParameterSerializerFactory;

import io.netty.channel.EventLoopGroup;

public class RpcClientConfig {

	protected RpcClient rpcClient;
	
	protected RegistryClient registryClient;
	
	//是否输出底层ping pong信息
	protected boolean printPingPongInfo = false;

	// 工作线程数
	protected int workThreadSize = 8;
	
	// 连接数
	protected int connectionSize = 4;
	
	//服务端地址
	protected String serverHost = "localhost";
	
	// 验证token
	protected String authToken = RpcConstant.DEFAULT_AUTH_TOKEN;
	
	//是否输出包信息
	protected boolean printEventbusPackLog = false;
	
	//共享线程组
	protected EventLoopGroup sharedEventLoopGroup;
	
	//代理调用处理器
	protected InvocationHandler invocationHandler = new  ProxyInvocationHandler(this);
	
	//代理管理器
	protected RpcProxyManager proxyManager = new  RpcProxyManager(this);
	
	//代理服务管理器
	protected RpcServiceManager serviceManager = new  RpcServiceManager(this);
	
	//代理服务供应器
	protected RpcServiceProvider serviceProvider= new  RpcServiceProvider(this);
	
	//接口信息解析器
	protected InterfaceInfoParser interfaceInfoParser = new  InterfaceInfoParser();
	
	//参数序列化器工厂
	protected ParameterSerializerFactory parameterSerializerFactory = new  DefaultParameterSerializerFactory();


	public RpcClient getRpcClient() {
		return rpcClient;
	}

	public void setRpcClient(RpcClient eventbusClient) {
		this.rpcClient = eventbusClient;
	}

	public boolean isPrintPingPongInfo() {
		return printPingPongInfo;
	}

	public void setPrintPingPongInfo(boolean printPingPongInfo) {
		this.printPingPongInfo = printPingPongInfo;
	}

	public int getWorkThreadSize() {
		return workThreadSize;
	}

	public void setWorkThreadSize(int workThreadSize) {
		this.workThreadSize = workThreadSize;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public int getConnectionSize() {
		return connectionSize;
	}
	
	public void setConnectionSize(int connectionSize) {
		this.connectionSize = connectionSize;
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public boolean isPrintEventbusPackLog() {
		return printEventbusPackLog;
	}

	public void setPrintEventbusPackLog(boolean printEventbusPackLog) {
		this.printEventbusPackLog = printEventbusPackLog;
	}
	
	public EventLoopGroup getSharedEventLoopGroup() {
		return sharedEventLoopGroup;
	}
	
	public void setSharedEventLoopGroup(EventLoopGroup sharedEventLoopGroup) {
		this.sharedEventLoopGroup = sharedEventLoopGroup;
	}
	
	public void setInvocationHandler(InvocationHandler invocationHandler) {
		this.invocationHandler = invocationHandler;
	}
	
	public InvocationHandler getInvocationHandler() {
		return invocationHandler;
	}
	
	public RpcProxyManager getProxyManager() {
		return proxyManager;
	}
	
	public void setProxyManager(RpcProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}
	
	public RpcServiceManager getServiceManager() {
		return serviceManager;
	}
	
	public void setServiceManager(RpcServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}
	
	public RpcServiceProvider getServiceProvider() {
		return serviceProvider;
	}
	
	public void setServiceProvider(RpcServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public RegistryClient getRegistryClient() {
		return registryClient;
	}

	public void setRegistryClient(RegistryClient registryClient) {
		this.registryClient = registryClient;
	}
	
	public InterfaceInfoParser getInterfaceInfoParser() {
		return interfaceInfoParser;
	}
	
	public void setInterfaceInfoParser(InterfaceInfoParser interfaceInfoParser) {
		this.interfaceInfoParser = interfaceInfoParser;
	}

	public ParameterSerializerFactory getParameterSerializerFactory() {
		return parameterSerializerFactory;
	}

	public void setParameterSerializerFactory(ParameterSerializerFactory parameterSerializerFactory) {
		this.parameterSerializerFactory = parameterSerializerFactory;
	}
	
	
	
}
