package com.ggx.router.server.config;

import com.ggx.group.server.SessionGroupServer;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.client.RouterClient;
import com.ggx.router.common.constant.RouterConstant;
import com.ggx.router.server.RouterServer;
import com.xzcode.ggserver.core.server.GGServer;

import io.netty.channel.EventLoopGroup;

/**
 * 路由服务器配置
 * 
 * @author zai 2019-12-05 10:33:40
 */
public class RouterServerConfig {

	// routerServer对象
	protected RouterServer routerServer;
	
	
	//业务服务端对象
	protected GGServer serviceServer;
	
	//是否开启业务服务端
	protected boolean enableServiceServer = false;
	
	// 转发路由客户端
	protected RouterClient forwardRouterClient;
	
	// 是否启用转发路由客户端
	protected boolean enableForwardRouterClient = false;
	
	//转发路由客户端所在路由组
	protected String forwardRouterGroupId = RouterConstant.DEFAULT_FORWARD_ROUTER_GROUP;

	// sessionGroupServer对象
	protected SessionGroupServer sessionGroupServer;

	// 是否输出底层ping pong信息
	protected boolean printPingPongInfo = false;

	// 工作线程数
	protected int workThreadSize = 8;

	// 连接数
	protected int connectionSize = 8;
	
	//共享的线程组
	private EventLoopGroup sharedEventLoopGroup;

	// 服务端口
	private int port = RouterConstant.DEFAULT_SERVER_PORT;

	// 认证token
	private String authToken = RouterConstant.DEFAULT_AUTH_TOKEN;

	protected String routerGroupId = RouterConstant.DEFAULT_ROUTER_GROUP;

	protected String actionIdPrefix;

	protected RegistryClient registryClient;

	public void setRegistryClient(RegistryClient registryClient) {
		this.registryClient = registryClient;
	}

	public RegistryClient getRegistryClient() {
		return registryClient;
	}

	public String getRouterGroupId() {
		return routerGroupId;
	}

	public void setRouterGroupId(String routerGroup) {
		this.routerGroupId = routerGroup;
	}

	public void setActionIdPrefix(String actionIdPrefix) {
		this.actionIdPrefix = actionIdPrefix;
	}

	public String getActionIdPrefix() {
		return actionIdPrefix;
	}

	public RouterServer getRouterServer() {
		return routerServer;
	}

	public void setRouterServer(RouterServer routerServer) {
		this.routerServer = routerServer;
	}

	public SessionGroupServer getSessionGroupServer() {
		return sessionGroupServer;
	}

	public void setSessionGroupServer(SessionGroupServer sessionGroupServer) {
		this.sessionGroupServer = sessionGroupServer;
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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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
	
	public RouterClient getForwardRouterClient() {
		return forwardRouterClient;
	}
	
	public void setForwardRouterClient(RouterClient forwardRouterClient) {
		this.forwardRouterClient = forwardRouterClient;
	}

	public boolean isEnableForwardRouterClient() {
		return enableForwardRouterClient;
	}

	public void setEnableForwardRouterClient(boolean enableForwardRouterClient) {
		this.enableForwardRouterClient = enableForwardRouterClient;
	}
	
	public String getForwardRouterGroupId() {
		return forwardRouterGroupId;
	}
	
	public void setForwardRouterGroupId(String forwardRouterClientGroupId) {
		this.forwardRouterGroupId = forwardRouterClientGroupId;
	}

	public GGServer getServiceServer() {
		return serviceServer;
	}

	public void setServiceServer(GGServer serviceServer) {
		this.serviceServer = serviceServer;
	}

	public boolean isEnableServiceServer() {
		return enableServiceServer;
	}

	public void setEnableServiceServer(boolean enableServiceServer) {
		this.enableServiceServer = enableServiceServer;
	}
	
	
	
	

	
}
