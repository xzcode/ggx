package com.xzcode.ggcloud.eventbus.server.config;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.registry.client.RegistryClient;
import com.xzcode.ggcloud.eventbus.server.EventbusServer;
import com.xzcode.ggcloud.eventbus.server.subscription.SubscriptionManager;
import com.xzcode.ggserver.core.server.port.PortChangeStrategy;

/**
 * 配置
 * 
 * 
 * @author zai
 * 2019-10-04 17:23:47
 */
public class EventbusServerConfig {
	
	//eventbusServer对象
	protected EventbusServer eventbusServer;
	
	//注册中心客户端对象
	protected RegistryClient registryClient;
	
	/**
	 * 事件组id
	 */
	protected String eventbusGroupId = EventbusConstant.DEFAULT_EVENTBUS_GROUP_ID;
	
	//sessionGroupServer对象
	protected SessionGroupServer sessionGroupServer;
	
	//订阅管理器
	protected SubscriptionManager subscriptionManager = new SubscriptionManager(this);
	
	//是否输出底层ping pong信息
	protected boolean printPingPongInfo = false;

	// 工作线程数
	protected int workThreadSize = 8;
	
	// 连接数
	protected int connectionSize = 8;
	
	//服务端口
	private int port = EventbusConstant.DEFAULT_SERVER_PORT;
	
	//认证token
	private String authToken = EventbusConstant.DEFAULT_AUTH_TOKEN;
	
	//是否输出包信息
	protected boolean printEventbusPackLog = false;
	
	//如果端口被占用是否更换端口并重新启动
	protected boolean 	changeAndRebootIfPortInUse = true;
	
	//端口更改策略
	protected String 	portChangeStrategy = PortChangeStrategy.RANDOM;
	
	//使用随机端口启动
	protected boolean 	bootWithRandomPort = false;
	
	
	public boolean isBootWithRandomPort() {
		return bootWithRandomPort;
	}
	
	public void setBootWithRandomPort(boolean bootWithRandomPort) {
		this.bootWithRandomPort = bootWithRandomPort;
	}
	
	
	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public EventbusServer getEventbusServer() {
		return eventbusServer;
	}
	
	public void setEventbusServer(EventbusServer eventbusServer) {
		this.eventbusServer = eventbusServer;
	}
	
	public SubscriptionManager getSubscriptionManager() {
		return subscriptionManager;
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

	public void setSubscriptionManager(SubscriptionManager subscriptionManager) {
		this.subscriptionManager = subscriptionManager;
	}

	public boolean isPrintEventbusPackLog() {
		return printEventbusPackLog;
	}

	public void setPrintEventbusPackLog(boolean printEventbusPackLog) {
		this.printEventbusPackLog = printEventbusPackLog;
	}
	
	public RegistryClient getRegistryClient() {
		return registryClient;
	}
	public void setRegistryClient(RegistryClient registryClient) {
		this.registryClient = registryClient;
	}
	
	public String getEventbusGroupId() {
		return eventbusGroupId;
	}
	
	public void setEventbusGroupId(String eventbusGroupId) {
		this.eventbusGroupId = eventbusGroupId;
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
	
	
}
