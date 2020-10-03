package com.ggx.rpc.server.config;

import com.ggx.core.server.port.PortChangeStrategy;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.registry.client.RegistryClient;
import com.ggx.rpc.common.constant.RpcConstant;
import com.ggx.rpc.server.RpcServer;

/**
 * 配置
 * 
 * 
 * @author zai
 * 2019-10-04 17:23:47
 */
public class RpcServerConfig {
	
	//eventbusServer对象
	protected RpcServer eventbusServer;
	
	//注册中心客户端对象
	protected RegistryClient registryClient;
	
	//sessionGroupServer对象
	protected SessionGroupServer sessionGroupServer;
	
	//是否输出底层ping pong信息
	protected boolean printPingPongInfo = false;

	// 工作线程数
	protected int workThreadSize = 8;
	
	// 连接数
	protected int connectionSize = 4;
	
	//认证token
	private String authToken = RpcConstant.DEFAULT_AUTH_TOKEN;
	
	//是否输出包信息
	protected boolean printEventbusPackLog = false;
	
	//如果端口被占用是否更换端口并重新启动
	protected boolean 	changeAndRebootIfPortInUse = true;
	
	//端口更改策略
	protected String 	portChangeStrategy = PortChangeStrategy.RANDOM;
	
	//使用随机端口启动
	protected boolean 	bootWithRandomPort = true;
	
	//服务组id
	protected String serviceGroupId;
	
	
	public int getPort() {
		return this.getSessionGroupServer().getConfig().getSessionServer().getConfig().getPort();
	}
	
	
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

	public RpcServer getEventbusServer() {
		return eventbusServer;
	}
	
	public void setEventbusServer(RpcServer eventbusServer) {
		this.eventbusServer = eventbusServer;
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

	public String getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(String rpcGroupId) {
		this.serviceGroupId = rpcGroupId;
	}
	
	
}
