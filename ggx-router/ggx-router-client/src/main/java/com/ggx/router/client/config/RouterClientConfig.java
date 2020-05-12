package com.ggx.router.client.config;

import java.util.UUID;

import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.filter.RouteReceiveMessageFilter;
import com.ggx.router.client.service.RouterServiceMatcher;
import com.ggx.router.client.service.RouterServiceProvider;
import com.ggx.router.client.service.impl.DefaultRegistryServicePorvider;
import com.ggx.router.client.service.impl.DefaultServicePorvider;
import com.ggx.router.client.service.impl.RouterServiceActionPrefixMatcher;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.manager.RouterServiceManager;
import com.ggx.router.common.constant.RouterConstant;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.impl.GGDefaultServer;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 路由客户端配置
 *
 * @author zai
 * 2020-04-14 17:18:45
 */
public class RouterClientConfig {

	//路由组id
	protected String routerGroupId;

	//注册中心客户端
	protected RegistryClient registryClient;

	// routerClient对象
	protected RouterClient routerClient;
	

	// 是否输出底层ping pong信息
	protected boolean printPingPongInfo = false;
	
	// 是否输出路由调试信息
	protected boolean printRouterInfo = false;

	// 工作线程数
	protected int workThreadSize = 8;

	// 连接数
	protected int connectionSize = 8;


	// 服务端地址
	protected String serverHost = RouterConstant.DEFAULT_SERVER_HOST;

	// 服务端端口
	protected int serverPort = RouterConstant.DEFAULT_SERVER_PORT;

	// 验证token
	protected String authToken = RouterConstant.DEFAULT_AUTH_TOKEN;

	/**
	 * 消息将被路由的服务器对象
	 */
	protected GGServer hostServer;

	//不参与路由的actionid
	protected String[] excludedActionId;

	//路由服务提供者
	protected RouterServiceProvider serviceProvider;

	//共享的线程组
	protected EventLoopGroup sharedEventLoopGroup;
	
	
	//路由服务匹配器
	protected RouterServiceMatcher routerServiceMatcher;
	
	//路由服务匹配器
	protected RouterServiceLoadblancer routerServiceLoadblancer;
	
	//路由服务管理器
	protected RouterServiceManager routerServiceManager;

	public RouterClientConfig(GGServer routingServer) {
		if (routingServer == null) {
			throw new NullPointerException("Parameter 'routingServer' cannot be null!!");
		}
		this.hostServer = routingServer;

	}

	/**
	 * 初始化
	 * 
	 * @author zai 2019-10-22 18:27:01
	 */
	public void init() {

		if (this.sharedEventLoopGroup == null) {
			this.sharedEventLoopGroup = new NioEventLoopGroup(workThreadSize, new GGThreadFactory("gg-router-", false));
		}
		
		this.hostServer.addBeforeDeserializeFilter(new RouteReceiveMessageFilter(this));

		if (routerGroupId == null) {
			routerGroupId = UUID.randomUUID().toString();
		}

		if (this.registryClient != null) {
			this.registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_GROUP_ID,getRouterGroupId());
			setServiceProvider(new DefaultRegistryServicePorvider(this));
		}
		
		if (this.routerServiceMatcher != null) {
			this.routerServiceMatcher = new RouterServiceActionPrefixMatcher();
		}
		
		if (this.serviceProvider == null) {
			this.serviceProvider = new DefaultServicePorvider(this);
		}
		
	}

	public RouterServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(RouterServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public GGServer getHostServer() {
		return hostServer;
	}


	public String[] getExcludedActionId() {
		return excludedActionId;
	}

	public void setExcludedActionId(String[] excludedRoutingActionRegex) {
		this.excludedActionId = excludedRoutingActionRegex;
	}

	public void setHostServer(GGServer routingServer) {
		this.hostServer = routingServer;
	}

	public RouterClient getRouterClient() {
		return routerClient;
	}

	public void setRouterClient(RouterClient routerClient) {
		this.routerClient = routerClient;
	}

	public String getRouterGroupId() {
		return routerGroupId;
	}

	public void setRouterGroupId(String routerGroupId) {
		this.routerGroupId = routerGroupId;
	}


	public RegistryClient getRegistryClient() {
		return registryClient;
	}

	public void setRegistryClient(RegistryClient discoveryClient) {
		this.registryClient = discoveryClient;
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

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int port) {
		this.serverPort = port;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public EventLoopGroup getSharedEventLoopGroup() {
		return sharedEventLoopGroup;
	}

	public void setSharedEventLoopGroup(EventLoopGroup sharedEventLoopGroup) {
		this.sharedEventLoopGroup = sharedEventLoopGroup;
	}

	public boolean isPrintRouterInfo() {
		return printRouterInfo;
	}

	public void setPrintRouterInfo(boolean printRouterInfo) {
		this.printRouterInfo = printRouterInfo;
	}
	
	public RouterServiceMatcher getRouterServiceMatcher() {
		return routerServiceMatcher;
	}
	
	public void setRouterServiceMatcher(RouterServiceMatcher routerServiceMatcher) {
		this.routerServiceMatcher = routerServiceMatcher;
	}
	
	public RouterServiceLoadblancer getRouterServiceLoadblancer() {
		return routerServiceLoadblancer;
	}
	
	public void setRouterServiceLoadblancer(RouterServiceLoadblancer routerServiceLoadblancer) {
		this.routerServiceLoadblancer = routerServiceLoadblancer;
	}
	
	public RouterServiceManager getRouterServiceManager() {
		return routerServiceManager;
	}
	
	public void setRouterServiceManager(RouterServiceManager routerServiceManager) {
		this.routerServiceManager = routerServiceManager;
	}
	
	
	
	

}
