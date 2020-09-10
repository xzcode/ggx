package com.ggx.eventbus.client.config;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.core.common.session.GGXSession;
import com.ggx.eventbus.client.EventbusClient;
import com.ggx.eventbus.client.subscriber.SubscriberManager;
import com.ggx.session.group.client.SessionGroupClient;

import io.netty.channel.EventLoopGroup;

/**
 * EventbusClient配置
 *
 * @author zai
 * 2020-04-10 11:58:47
 */
public class EventbusClientConfig {

	// eventbusClient对象
	protected EventbusClient eventbusClient;
	
	// sessionGroupClient对象
	protected SessionGroupClient sessionGroupClient;

	//是否输出底层ping pong信息
	protected boolean printPingPongInfo = false;

	// 工作线程数
	protected int workThreadSize = 8;
	
	// 连接数
	protected int connectionSize = 4;
	
	
	protected GGXSession defaultClientSession;


	// 订阅者管理器
	protected SubscriberManager subscribeManager;

	//服务端地址
	protected String serverHost = "localhost";
	
	//服务端端口
	protected int serverPort = EventbusConstant.DEFAULT_SERVER_PORT;
	
	// 验证token
	protected String authToken = EventbusConstant.DEFAULT_AUTH_TOKEN;
	
	
	//是否输出包信息
	protected boolean printEventbusPackLog = false;
	
	//共享线程组
	protected EventLoopGroup sharedEventLoopGroup;


	public EventbusClient getEventbusClient() {
		return eventbusClient;
	}

	public void setEventbusClient(EventbusClient eventbusClient) {
		this.eventbusClient = eventbusClient;
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

	public SubscriberManager getSubscribeManager() {
		return subscribeManager;
	}

	public void setSubscribeManager(SubscriberManager subscriptionManager) {
		this.subscribeManager = subscriptionManager;
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

	public SessionGroupClient getSessionGroupClient() {
		return sessionGroupClient;
	}

	public void setSessionGroupClient(SessionGroupClient sessionGroupClient) {
		this.sessionGroupClient = sessionGroupClient;
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

	public boolean isPrintEventbusPackLog() {
		return printEventbusPackLog;
	}

	public void setPrintEventbusPackLog(boolean printEventbusPackLog) {
		this.printEventbusPackLog = printEventbusPackLog;
	}
	
	public void setDefaultClientSession(GGXSession defaultClientSession) {
		this.defaultClientSession = defaultClientSession;
	}
	
	public GGXSession getDefaultClientSession() {
		return defaultClientSession;
	}

	public EventLoopGroup getSharedEventLoopGroup() {
		return sharedEventLoopGroup;
	}
	
	public void setSharedEventLoopGroup(EventLoopGroup sharedEventLoopGroup) {
		this.sharedEventLoopGroup = sharedEventLoopGroup;
	}
	
}
