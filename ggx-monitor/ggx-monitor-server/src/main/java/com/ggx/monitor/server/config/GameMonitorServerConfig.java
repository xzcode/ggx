package com.ggx.monitor.server.config;

import com.ggx.monitor.common.constant.GameMonitorConstant;
import com.ggx.monitor.common.data.manager.MonitorDataCenter;
import com.xzcode.ggserver.core.server.GGServer;

/**
 * 配置
 * 
 * 
 * @author zai
 * 2019-10-04 17:23:47
 */
public class GameMonitorServerConfig {
	
	//ggserver对象
	private GGServer server;
	
	//是否打印pingpong包信息
	protected boolean 	printPingPongInfo = false;
	
	//服务管理器
	private MonitorDataCenter monitorDataCenter = new MonitorDataCenter();
	
	//服务端口
	private int port = GameMonitorConstant.DEFAULT_SERVER_PORT;;
	
	//认证token
	private String authToken = GameMonitorConstant.DEFAULT_AUTH_TOKEN;
	
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
	
	public GGServer getServer() {
		return server;
	}
	
	public void setServer(GGServer server) {
		this.server = server;
	}

	public MonitorDataCenter getMonitorDataCenter() {
		return monitorDataCenter;
	}

	public void setMonitorDataCenter(MonitorDataCenter monitorDataCenter) {
		this.monitorDataCenter = monitorDataCenter;
	}

	public boolean isPrintPingPongInfo() {
		return printPingPongInfo;
	}
	
	public void setPrintPingPongInfo(boolean printPingPongInfo) {
		this.printPingPongInfo = printPingPongInfo;
	}
	

}
