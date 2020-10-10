package com.ggx.router.client.config;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.utils.GGXIdUtil;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.filter.RouterClientHostServerReceiveMessageFilter;
import com.ggx.router.client.service.RouterServiceMatcher;
import com.ggx.router.client.service.RouterServiceProvider;
import com.ggx.router.client.service.impl.DefaultRegistryServicePorvider;
import com.ggx.router.client.service.impl.DefaultServicePorvider;
import com.ggx.router.client.service.impl.RegistrySingleServicePorvider;
import com.ggx.router.client.service.impl.RouterServiceActionPrefixMatcher;
import com.ggx.router.client.service.loadblancer.RouterServiceLoadbalancer;
import com.ggx.router.client.service.loadblancer.constant.RouterServiceLoadblanceType;
import com.ggx.router.client.service.loadblancer.constant.RouterServiceProviderType;
import com.ggx.router.client.service.loadblancer.factory.DefaultRouterServiceLoadblancerFactory;
import com.ggx.router.client.service.loadblancer.factory.RouterServiceLoadblancerFactory;
import com.ggx.router.client.service.manager.RouterServiceManager;
import com.ggx.router.common.constant.RouterConstant;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 路由客户端配置
 *
 * @author zai 2020-04-14 17:18:45
 */
public class RouterClientConfig {

	// 路由组id
	protected String routerGroupId;

	// 注册中心客户端
	protected RegistryClient registryClient;

	// routerClient对象
	protected RouterClient routerClient;

	// eventbus组客户端
	protected EventbusGroupClient eventbusGroupClient;

	// 是否输出底层ping pong信息
	protected boolean printPingPongInfo = false;

	// 是否输出路由调试信息
	protected boolean printRouterInfo = false;

	// 工作线程数
	protected int workThreadSize = 8;

	// 连接数
	protected int connectionSize = 4;

	// 服务端地址
	protected String serverHost = RouterConstant.DEFAULT_SERVER_HOST;

	// 服务端端口
	protected int serverPort = RouterConstant.DEFAULT_SERVER_PORT;

	// 验证token
	protected String authToken = RouterConstant.DEFAULT_AUTH_TOKEN;

	/**
	 * 消息将被路由的服务器对象
	 */
	protected GGXCoreServer hostServer;

	// 不参与路由的actionid
	protected String[] excludedActionId;

	// 路由服务提供者
	protected RouterServiceProvider serviceProvider;

	// 路由服务提供者类型
	protected String serviceProviderType = RouterServiceProviderType.REGISTRY_MULTI_SERVICES;

	// 共享的线程组
	protected EventLoopGroup sharedEventLoopGroup;

	// 路由服务匹配器
	protected RouterServiceMatcher routerServiceMatcher = new RouterServiceActionPrefixMatcher();

	// 路由服务负载均衡器类型
	protected String routerServiceLoadblanceType = RouterServiceLoadblanceType.HASH;

	// 路由服务管理器
	protected RouterServiceManager routerServiceManager = new RouterServiceManager(this);

	// 会话断开请求传递是否开启
	protected boolean sessionDisconnectTransferRequestEnabled = true;

	// 会话断开推送传递是否开启
	protected boolean sessionDisconnectTransferResponseEnabled = false;

	// 路由服务负载均衡器工厂
	protected RouterServiceLoadblancerFactory routerServiceLoadblancerFactory = new DefaultRouterServiceLoadblancerFactory(
			this);

	public RouterClientConfig() {

	}

	public RouterClientConfig(GGXCoreServer routingServer) {
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
			this.sharedEventLoopGroup = new NioEventLoopGroup(workThreadSize,
					new GGXThreadFactory("ggx-router-", false));
		}

		this.hostServer.addFilter(new RouterClientHostServerReceiveMessageFilter(this));

		if (routerGroupId == null) {
			routerGroupId = GGXIdUtil.newRandomStringId24();
		}

		if (this.registryClient != null) {
			this.registryClient.addCustomData(RouterServiceCustomDataKeys.FORWARD_ROUTER_GROUP_ID, getRouterGroupId());
			setServiceProvider(new DefaultRegistryServicePorvider(this));
		}

		if (this.routerServiceMatcher != null) {
			this.routerServiceMatcher = new RouterServiceActionPrefixMatcher();
		}

		if (this.serviceProvider == null) {
			if (this.registryClient != null) {
				if (this.routerServiceLoadblanceType.contentEquals(RouterServiceProviderType.REGISTRY_MULTI_SERVICES)) {
					this.serviceProvider = new DefaultRegistryServicePorvider(this);
				} else if (this.routerServiceLoadblanceType
						.contentEquals(RouterServiceProviderType.REGISTRY_SINGLE_SERVICE)) {
					this.serviceProvider = new RegistrySingleServicePorvider(this);
				}
			} else {
				this.serviceProvider = new DefaultServicePorvider(this);
			}
		}

	}

	public RouterServiceLoadbalancer getRouterServiceLoadblancer() {
		return this.getRouterServiceLoadblancerFactory().getLoadblancer(this.getRouterServiceLoadblanceType());
	}

	public TaskExecutor getSingleThreadTaskExecutor() {
		return this.getHostServer().getTaskExecutor().nextEvecutor();
	}

	public RouterServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(RouterServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public GGXCoreServer getHostServer() {
		return hostServer;
	}

	public String[] getExcludedActionId() {
		return excludedActionId;
	}

	public void setExcludedActionId(String[] excludedRoutingActionRegex) {
		this.excludedActionId = excludedRoutingActionRegex;
	}

	public void setHostServer(GGXCoreServer routingServer) {
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

	public String getRouterServiceLoadblanceType() {
		return routerServiceLoadblanceType;
	}

	public void setRouterServiceLoadblanceType(String routerServiceLoadblanceType) {
		this.routerServiceLoadblanceType = routerServiceLoadblanceType;
	}

	public RouterServiceManager getRouterServiceManager() {
		return routerServiceManager;
	}

	public void setRouterServiceManager(RouterServiceManager routerServiceManager) {
		this.routerServiceManager = routerServiceManager;
	}

	public EventbusGroupClient getEventbusGroupClient() {
		return eventbusGroupClient;
	}

	public void setEventbusGroupClient(EventbusGroupClient eventbusGroupClient) {
		this.eventbusGroupClient = eventbusGroupClient;
	}

	public boolean isSessionDisconnectTransferRequestEnabled() {
		return sessionDisconnectTransferRequestEnabled;
	}

	public void setSessionDisconnectTransferRequestEnabled(boolean sessionDisconnectTransferReuestEnabled) {
		this.sessionDisconnectTransferRequestEnabled = sessionDisconnectTransferReuestEnabled;
	}

	public boolean isSessionDisconnectTransferResponseEnabled() {
		return sessionDisconnectTransferResponseEnabled;
	}

	public void setSessionDisconnectTransferResponseEnabled(boolean sessionDisconnectTransferRsponseEnabled) {
		this.sessionDisconnectTransferResponseEnabled = sessionDisconnectTransferRsponseEnabled;
	}

	public RouterServiceLoadblancerFactory getRouterServiceLoadblancerFactory() {
		return routerServiceLoadblancerFactory;
	}

	public void setRouterServiceLoadblancerFactory(RouterServiceLoadblancerFactory routerServiceLoadblancerFactory) {
		this.routerServiceLoadblancerFactory = routerServiceLoadblancerFactory;
	}

}
