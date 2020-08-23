package com.ggx.group.server.config;

import java.util.concurrent.ThreadFactory;

import com.ggx.group.common.constant.GGSesssionGroupConstant;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.server.transfer.custom.CustomDataTransferHandler;
import com.xzcode.ggserver.core.server.GGXCoreServer;
import com.xzcode.ggserver.core.server.port.PortChangeStrategy;

import io.netty.channel.EventLoopGroup;

/**
 * 总配置
 *
 * @author zai
 * 2020-04-08 11:53:31
 */
public class SessionGroupServerConfig {
	
	//sessionServer对象
	private GGXCoreServer sessionServer;
	
	//业务服务端对象
	private GGXCoreServer serviceServer;
	
	//是否开启业务服务端
	private boolean enableServiceServer = false;
	
	//是否打印pingpong包信息
	protected boolean printPingPongInfo = false;
	
	//会话组管理器
	private GGSessionGroupManager sessionGroupManager;
	
	
	//自定义数据传输处理器
	private CustomDataTransferHandler customDataTransferHandler;
	
	//是否开启自定义数据传输处理器
	private boolean enableCustomDataTransferHandler = false;
	
	//工作线程数
	protected int workThreadSize = 8;
	
	
	//服务端口
	private int port = GGSesssionGroupConstant.DEFAULT_SERVER_PORT;
	
	//认证token
	private String authToken = GGSesssionGroupConstant.DEFAULT_AUTH_TOKEN;
	
	//工作线程工厂
	protected ThreadFactory workThreadFactory;
	
	//工作线程组
	protected EventLoopGroup workEventLoopGroup;
	
	
	//是否输出会话组包信息
	protected boolean printSessionGroupPackLog = false;
	
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
	
	public GGXCoreServer getSessionServer() {
		return sessionServer;
	}
	
	public void setSessionServer(GGXCoreServer sessionServer) {
		this.sessionServer = sessionServer;
	}
	
	public GGSessionGroupManager getSessionGroupManager() {
		return sessionGroupManager;
	}
	
	public void setSessionGroupManager(GGSessionGroupManager sessionGroupManager) {
		this.sessionGroupManager = sessionGroupManager;
	}

	public boolean isPrintPingPongInfo() {
		return printPingPongInfo;
	}
	
	public void setPrintPingPongInfo(boolean printPingPongInfo) {
		this.printPingPongInfo = printPingPongInfo;
	}
	
	
	public GGXCoreServer getServiceServer() {
		return serviceServer;
	}
	
	public void setServiceServer(GGXCoreServer serviceServer) {
		this.serviceServer = serviceServer;
	}

	public boolean isEnableServiceServer() {
		return enableServiceServer;
	}

	public void setEnableServiceServer(boolean enableServiceService) {
		this.enableServiceServer = enableServiceService;
	}

	public int getWorkThreadSize() {
		return workThreadSize;
	}

	public void setWorkThreadSize(int workThreadSize) {
		this.workThreadSize = workThreadSize;
	}

	public ThreadFactory getWorkThreadFactory() {
		return workThreadFactory;
	}
	
	public void setWorkThreadFactory(ThreadFactory workThreadFactory) {
		this.workThreadFactory = workThreadFactory;
	}

	public boolean isPrintSessionGroupPackLog() {
		return printSessionGroupPackLog;
	}

	public void setPrintSessionGroupPackLog(boolean printSessionGroupPackLog) {
		this.printSessionGroupPackLog = printSessionGroupPackLog;
	}

	public EventLoopGroup getWorkEventLoopGroup() {
		return workEventLoopGroup;
	}

	public void setWorkEventLoopGroup(EventLoopGroup workEventLoopGroup) {
		this.workEventLoopGroup = workEventLoopGroup;
	}

	public CustomDataTransferHandler getCustomDataTransferHandler() {
		return customDataTransferHandler;
	}

	public void setCustomDataTransferHandler(CustomDataTransferHandler customDataTransferHandler) {
		this.customDataTransferHandler = customDataTransferHandler;
	}

	public boolean isEnableCustomDataTransferHandler() {
		return enableCustomDataTransferHandler;
	}

	public void setEnableCustomDataTransferHandler(boolean enableCustomDataTransferHandler) {
		this.enableCustomDataTransferHandler = enableCustomDataTransferHandler;
	}
	
	
	public String getPortChangeStrategy() {
		return portChangeStrategy;
	}

	public boolean isChangeAndRebootIfPortInUse() {
		return changeAndRebootIfPortInUse;
	}

	public void setChangeAndRebootIfPortInUse(boolean changeAndRebootIfPortInUse) {
		this.changeAndRebootIfPortInUse = changeAndRebootIfPortInUse;
	}

	public void setPortChangeStrategy(String portChangeStrategy) {
		this.portChangeStrategy = portChangeStrategy;
	}
	
}
