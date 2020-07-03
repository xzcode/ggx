package com.ggx.admin.collector.server.config;

import com.ggx.admin.common.collector.constant.GGXAdminCollectorConstant;
import com.xzcode.ggserver.core.server.GGServer;

/**
 * 配置
 * 
 * 
 * @author zai
 * 2019-10-04 17:23:47
 */
public class GGXAdminCollectorServerConfig {
	
	//ggserver对象
	private GGServer server;
	
	//是否打印pingpong包信息
	protected boolean 	printPingPongInfo = false;
	
	//服务端口
	private int port = GGXAdminCollectorConstant.DEFAULT_SERVER_PORT;;
	
	//认证token
	private String authToken = GGXAdminCollectorConstant.DEFAULT_AUTH_TOKEN;
	
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


	public boolean isPrintPingPongInfo() {
		return printPingPongInfo;
	}
	
	public void setPrintPingPongInfo(boolean printPingPongInfo) {
		this.printPingPongInfo = printPingPongInfo;
	}

}
