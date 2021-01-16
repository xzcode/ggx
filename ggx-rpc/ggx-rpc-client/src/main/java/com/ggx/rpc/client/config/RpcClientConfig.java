package com.ggx.rpc.client.config;

import com.ggx.core.common.executor.DefaultTaskExecutor;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.registry.client.RegistryClient;
import com.ggx.rpc.client.RpcClient;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.rpc.client.service.InterfaceServiceGroupCache;
import com.ggx.rpc.client.service.RpcServiceClassCache;
import com.ggx.rpc.client.service.RpcServiceManager;
import com.ggx.rpc.client.service.callback.RpcMethodCallbackManager;
import com.ggx.rpc.client.service.fallback.DefaultFallbackInstanceFactory;
import com.ggx.rpc.client.service.fallback.FallbackInstanceFactory;
import com.ggx.rpc.client.service.provider.RpcServiceProvider;
import com.ggx.rpc.common.Interfaceinfo.InterfaceInfoParser;
import com.ggx.rpc.common.constant.RpcConstant;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.rpc.common.serializer.factory.impl.DefaultParameterSerializerFactory;
import com.ggx.util.thread.GGXThreadFactory;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

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
	
	//服务组id
	protected String rpcServiceGroupId = RpcConstant.DEFAULT_RPC_SERVICE_GROUP_ID;
	
	//是否输出包信息
	protected boolean printEventbusPackLog = false;
	
	//共享线程组
	protected EventLoopGroup sharedEventLoopGroup;
	
	
	//任务执行器
	protected TaskExecutor taskExecutor;
	
	//rpc调用超时时长 毫秒ms
	protected long rpcTimeout = 10L * 1000L;
	
	//代理管理器
	protected RpcProxyManager proxyManager = new  RpcProxyManager(this);
	
	//代理服务管理器
	protected RpcServiceManager serviceManager = new  RpcServiceManager(this);
	
	//代理服务供应器
	protected RpcServiceProvider serviceProvider;
	
	//接口信息解析器
	protected InterfaceInfoParser interfaceInfoParser = new  InterfaceInfoParser();
	
	//接口与rpc服务关联缓存
	protected InterfaceServiceGroupCache interfaceServiceGroupCache = new  InterfaceServiceGroupCache();
	
	//rpc服务相关类型缓存
	protected RpcServiceClassCache classCache = new  RpcServiceClassCache();
	
	//rpc服务备用实例工厂
	protected FallbackInstanceFactory fallbackInstanceFactory = new  DefaultFallbackInstanceFactory();
	
	//参数序列化器工厂
	protected ParameterSerializerFactory parameterSerializerFactory = new  DefaultParameterSerializerFactory();
	
	//RPC方法回调管理器
	protected RpcMethodCallbackManager rpcMethodCallbackManager = new RpcMethodCallbackManager(this);
	
	public RpcClientConfig() {
		System.out.println();
	}
	

	public void init() {
		

		if (this.getSharedEventLoopGroup() == null) {
			this.setSharedEventLoopGroup(new NioEventLoopGroup(this.getWorkThreadSize(), new GGXThreadFactory("ggx-rpc-cli-", false)));
		}
		
		if (this.getTaskExecutor() == null) {
			this.setTaskExecutor(new DefaultTaskExecutor(this.getSharedEventLoopGroup()));
		}
		
		if (this.getRegistryClient() != null) {
			if (this.getServiceProvider() == null) {
				this.setServiceProvider(new RpcServiceProvider(this));
			}
		}
	}


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
	
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
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
	
	
	public InterfaceServiceGroupCache getInterfaceServiceGroupCache() {
		return interfaceServiceGroupCache;
	}
	public void setInterfaceServiceGroupCache(InterfaceServiceGroupCache interfaceServiceCache) {
		this.interfaceServiceGroupCache = interfaceServiceCache;
	}
	
	public RpcServiceClassCache getClassCache() {
		return classCache;
	}
	
	public void setClassCache(RpcServiceClassCache classCache) {
		this.classCache = classCache;
	}
	
	public FallbackInstanceFactory getFallbackInstanceFactory() {
		return fallbackInstanceFactory;
	}
	
	public void setFallbackInstanceFactory(FallbackInstanceFactory fallbackInstanceFactory) {
		this.fallbackInstanceFactory = fallbackInstanceFactory;
	}

	public long getRpcTimeout() {
		return rpcTimeout;
	}

	public void setRpcTimeout(long rpcTimeout) {
		this.rpcTimeout = rpcTimeout;
	}
	
	public RpcMethodCallbackManager getRpcMethodCallbackManager() {
		return rpcMethodCallbackManager;
	}
	public void setRpcMethodCallbackManager(RpcMethodCallbackManager rpcMethodCallbackManager) {
		this.rpcMethodCallbackManager = rpcMethodCallbackManager;
	}

	public String getRpcServiceGroupId() {
		return rpcServiceGroupId;
	}
	
	public void setRpcServiceGroupId(String serviceGroupId) {
		this.rpcServiceGroupId = serviceGroupId;
	}
	
}
