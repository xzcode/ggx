package com.xzcode.ggcloud.router.server.config;

import com.xzcode.ggcloud.discovery.client.DiscoveryClient;
import com.xzcode.ggcloud.router.common.constant.GGRouterConstant;
import com.xzcode.ggcloud.router.server.RouterServer;
import com.xzcode.ggcloud.session.group.server.SessionGroupServer;

import io.netty.channel.EventLoopGroup;

/**
 * 路由服务器配置
 * 
 * @author zai 2019-12-05 10:33:40
 */
public class RouterServerConfig {

	// routerServer对象
	protected RouterServer routerServer;

	// sessionGroupServer对象
	protected SessionGroupServer sessionGroupServer;

	// 是否输出底层ping pong信息
	protected boolean printPingPongInfo = false;

	// 工作线程数
	protected int workThreadSize = 8;

	// 连接数
	protected int connectionSize = 8;
	
	/**
	 * 共享的线程组
	 */
	private EventLoopGroup sharedEventLoopGroup;

	// 服务端口
	private int port = GGRouterConstant.DEFAULT_SERVER_PORT;

	// 认证token
	private String authToken = GGRouterConstant.DEFAULT_AUTH_TOKEN;

	protected String routerGroupId;

	protected String actionIdPrefix;

	protected DiscoveryClient discoveryClient;

	public void setDiscoveryClient(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	public DiscoveryClient getDiscoveryClient() {
		return discoveryClient;
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

	
}
