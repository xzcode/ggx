package com.ggx.session.group.client.config;

import java.util.concurrent.ThreadFactory;

import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.common.utils.GGXIdUtil;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.group.common.constant.GGSesssionGroupConstant;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.session.group.client.SessionGroupClient;

import io.netty.channel.EventLoopGroup;

/**
 * 配置
 * 
 * 
 * @author zai 2019-10-04 17:23:47
 */
public class SessionGroupClientConfig {

	// 本地客户端名称
	protected String sessionGroupClientName = SessionGroupClient.class.getSimpleName();

	// 目标服务端名称
	protected String targetServerName = "unknown";

	// 会话组客户端
	protected SessionGroupClient sessionGroupClient;

	// 会话客户端
	protected GGXCoreClient sessionClient;

	// 开启业务客户端
	protected boolean enableServiceClient;

	// 业务客户端
	protected GGXCoreClient serviceClient;

	// 开启业务服务端
	protected boolean enableServiceServer;

	// 业务客户端
	protected GGXCoreServer serviceServer;

	// 会话组管理器
	protected GGSessionGroupManager sessionGroupManager;

	// 服务器域名
	protected String serverHost = "localhost";

	// 服务器端口
	protected int serverPort = GGSesssionGroupConstant.DEFAULT_SERVER_PORT;

	// 工作线程数
	protected int workThreadSize = 8;

	// 工作线程工厂
	protected ThreadFactory workThreadFactory;

	/**
	 * 工作线程组
	 */
	protected EventLoopGroup workEventLoopGroup;

	// 连接数
	protected int connectionSize = 4;

	// 重连周期 毫秒
	protected long reconnectInterval = 10L * 1000L;

	protected boolean printPingPongInfo = false;

	// 会话组id
	protected String sessionGroupId = GGXIdUtil.newRandomStringId24();

	// 验证token
	protected String authToken = GGSesssionGroupConstant.DEFAULT_AUTH_TOKEN;

	// 指令前缀
	protected String serviceActionIdPrefix;

	public String getServiceActionIdPrefix() {
		return serviceActionIdPrefix;
	}

	public void setServiceActionIdPrefix(String actionIdPrefix) {
		this.serviceActionIdPrefix = actionIdPrefix;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public boolean isPrintPingPongInfo() {
		return printPingPongInfo;
	}

	public void setPrintPingPongInfo(boolean printPingPongInfo) {
		this.printPingPongInfo = printPingPongInfo;
	}

	public String getSessionGroupId() {
		return sessionGroupId;
	}

	public void setSessionGroupId(String sessionGroupId) {
		this.sessionGroupId = sessionGroupId;
	}

	public void setSessionClient(GGXCoreClient sessionClient) {
		this.sessionClient = sessionClient;
	}

	public GGXCoreClient getSessionClient() {
		return sessionClient;
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

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getConnectionSize() {
		return connectionSize;
	}

	public void setConnectionSize(int connectionSize) {
		this.connectionSize = connectionSize;
	}

	public void setSessionGroupManager(GGSessionGroupManager sessionGroupManager) {
		this.sessionGroupManager = sessionGroupManager;
	}

	public GGSessionGroupManager getSessionGroupManager() {
		return sessionGroupManager;
	}

	public GGXCoreClient getServiceClient() {
		return serviceClient;
	}

	public void setServiceClient(GGXCoreClient serviceClient) {
		this.serviceClient = serviceClient;
	}

	public boolean isEnableServiceClient() {
		return enableServiceClient;
	}

	public void setEnableServiceClient(boolean enableServiceClient) {
		this.enableServiceClient = enableServiceClient;
	}

	public void setSessionGroupClient(SessionGroupClient sessionGroupClient) {
		this.sessionGroupClient = sessionGroupClient;
	}

	public SessionGroupClient getSessionGroupClient() {
		return sessionGroupClient;
	}

	public int getWorkThreadSize() {
		return workThreadSize;
	}

	public void setWorkThreadSize(int workThreadSize) {
		this.workThreadSize = workThreadSize;
	}

	public long getReconnectInterval() {
		return reconnectInterval;
	}

	public void setReconnectInterval(long reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
	}

	public ThreadFactory getWorkThreadFactory() {
		return workThreadFactory;
	}

	public void setWorkThreadFactory(ThreadFactory workThreadFactory) {
		this.workThreadFactory = workThreadFactory;
	}

	public void setEnableServiceServer(boolean enableServiceServer) {
		this.enableServiceServer = enableServiceServer;
	}

	public boolean isEnableServiceServer() {
		return enableServiceServer;
	}

	public GGXCoreServer getServiceServer() {
		return serviceServer;
	}

	public void setServiceServer(GGXCoreServer serviceServer) {
		this.serviceServer = serviceServer;
	}

	public EventLoopGroup getWorkEventLoopGroup() {
		return workEventLoopGroup;
	}

	public void setWorkEventLoopGroup(EventLoopGroup workEventLoopGroup) {
		this.workEventLoopGroup = workEventLoopGroup;
	}

	public String getSessionGroupClientName() {
		return sessionGroupClientName;
	}

	public void setSessionGroupClientName(String sessionGroupClientName) {
		this.sessionGroupClientName = sessionGroupClientName;
	}

	public String getTargetServerName() {
		return targetServerName;
	}

	public void setTargetServerName(String targetServerName) {
		this.targetServerName = targetServerName;
	}
}
