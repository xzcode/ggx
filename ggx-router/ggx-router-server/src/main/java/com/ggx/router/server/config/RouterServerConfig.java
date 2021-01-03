package com.ggx.router.server.config;

import java.util.ArrayList;
import java.util.List;

import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.port.PortChangeStrategy;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.common.constant.RouterConstant;
import com.ggx.router.server.RouterServer;

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
	
	//自定义业务服务端
	protected GGXCoreServer hostServer;

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
	
	private String[] scanPackages;
	
	//忽略处理的指令前缀
	protected List<String> ignoreActionIdPrefixes = new ArrayList<>();

	protected RegistryClient registryClient;
	
	//如果端口被占用是否更换端口并重新启动
	protected boolean 	changeAndRebootIfPortInUse = true;
	
	//端口更改策略
	protected String 	portChangeStrategy = PortChangeStrategy.RANDOM;
	
	//使用随机端口启动
	protected boolean 	bootWithRandomPort = false;
	
	//会话断开请求传递是否开启
	protected boolean 	sessionDisconnectTransferReuestEnabled = true;
	
	//会话断开推送传递是否开启
	protected boolean 	sessionDisconnectTransferResponseEnabled = false;
	
	public boolean isBootWithRandomPort() {
		return bootWithRandomPort;
	}

	public void setBootWithRandomPort(boolean bootWithRandomPort) {
		this.bootWithRandomPort = bootWithRandomPort;
	}

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

	public boolean isChangeAndRebootIfPortInUse() {
		return changeAndRebootIfPortInUse;
	}

	public void setChangeAndRebootIfPortInUse(boolean changeAndRebootIfPortInUse) {
		this.changeAndRebootIfPortInUse = changeAndRebootIfPortInUse;
	}

	public String getPortChangeStrategy() {
		return portChangeStrategy;
	}

	public void setPortChangeStrategy(String portChangeStrategy) {
		this.portChangeStrategy = portChangeStrategy;
	}

	public boolean isSessionDisconnectTransferReuestEnabled() {
		return sessionDisconnectTransferReuestEnabled;
	}

	public void setSessionDisconnectTransferReuestEnabled(boolean sessionDisconnectTransferReuestEnabled) {
		this.sessionDisconnectTransferReuestEnabled = sessionDisconnectTransferReuestEnabled;
	}

	public boolean isSessionDisconnectTransferResponseEnabled() {
		return sessionDisconnectTransferResponseEnabled;
	}

	public void setSessionDisconnectTransferResponseEnabled(boolean sessionDisconnectTransferRsponseEnabled) {
		this.sessionDisconnectTransferResponseEnabled = sessionDisconnectTransferRsponseEnabled;
	}
	
	public List<String> getIgnoreActionIdPrefixes() {
		return ignoreActionIdPrefixes;
	}
	
	public void setIgnoreActionIdPrefixes(List<String> ignoreActionIdPrefixes) {
		this.ignoreActionIdPrefixes = ignoreActionIdPrefixes;
	}
	
	public GGXCoreServer getHostServer() {
		return hostServer;
	}
	
	public void setHostServer(GGXCoreServer hostServer) {
		this.hostServer = hostServer;
	}
	
	public String[] getScanPackages() {
		return scanPackages;
	}
	
	public void setScanPackages(String[] scanPackages) {
		this.scanPackages = scanPackages;
	}
	
}
