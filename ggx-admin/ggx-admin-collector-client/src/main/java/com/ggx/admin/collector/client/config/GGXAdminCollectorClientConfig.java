package com.ggx.admin.collector.client.config;

import com.ggx.admin.collector.client.GGXAdminCollectorClient;
import com.ggx.admin.collector.client.collector.task.CollectorTaskManager;
import com.ggx.admin.common.collector.constant.GGXAdminCollectorConstant;
import com.ggx.core.client.GGClient;
import com.ggx.core.common.executor.DefaultTaskExecutor;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.client.RegistryClient;

/**
 * 游戏监控客户端配置
 *
 * @author zai 2020-04-23 14:45:15
 */
public class GGXAdminCollectorClientConfig {

	// gameMonitorClient对象
	protected GGXAdminCollectorClient collectorClient;
	
	protected CollectorTaskManager collectorTaskManager = new CollectorTaskManager(this);

	protected boolean printPingPongInfo = false;

	// RegistryClient对象
	protected RegistryClient registryClient;

	protected GGClient serviceClient;

	// 任务执行器
	protected TaskExecutor taskExecutor = new DefaultTaskExecutor("admin-collector-task-", 1);

	// GGSession对象
	protected GGSession session;

	// 是否打印pingpong包信息
	protected boolean pingPongEnabled = false;

	// 客户端汇报超时时间(秒)
	protected long clientReportInterval = 30L * 1000L;

	// 重连间隔-秒
	protected long reconnectInterval = 5L * 1000L;

	// 尝试重新注册周期，ms
	protected long tryRegisterInterval = 10L * 1000L;

	// 服务端地址
	protected String serverHost = "localhost";

	// 服务端端口
	protected int serverPort = GGXAdminCollectorConstant.DEFAULT_SERVER_PORT;

	// 验证token
	protected String authToken = GGXAdminCollectorConstant.DEFAULT_AUTH_TOKEN;
	
	
	protected boolean authed = false;
	
	
	public String getServiceId() {
		return this.registryClient.getServiceId();
	}

	public long getClientReportInterval() {
		return clientReportInterval;
	}

	public void setClientReportInterval(long clientReportInterval) {
		this.clientReportInterval = clientReportInterval;
	}

	public long getReconnectInterval() {
		return reconnectInterval;
	}

	public void setReconnectInterval(long reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
	}

	public long getTryRegisterInterval() {
		return tryRegisterInterval;
	}

	public void setTryRegisterInterval(long tryRegisterInterval) {
		this.tryRegisterInterval = tryRegisterInterval;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setSession(GGSession session) {
		this.session = session;
	}

	public GGSession getSession() {
		return session;
	}

	public GGXAdminCollectorClient getCollectorClient() {
		return collectorClient;
	}

	public void setCollectorClient(GGXAdminCollectorClient discoveryClient) {
		this.collectorClient = discoveryClient;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public boolean isPingPongEnabled() {
		return pingPongEnabled;
	}

	public void setPingPongEnabled(boolean pingPongEnabled) {
		this.pingPongEnabled = pingPongEnabled;
	}

	public boolean isPrintPingPongInfo() {
		return printPingPongInfo;
	}

	public void setPrintPingPongInfo(boolean printPingPongInfo) {
		this.printPingPongInfo = printPingPongInfo;
	}

	public GGClient getServiceClient() {
		return serviceClient;
	}

	public void setServiceClient(GGClient serviceClient) {
		this.serviceClient = serviceClient;
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

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public RegistryClient getRegistryClient() {
		return registryClient;
	}

	public void setRegistryClient(RegistryClient registryClient) {
		this.registryClient = registryClient;
	}

	public CollectorTaskManager getCollectorTaskManager() {
		return collectorTaskManager;
	}

	public void setCollectorTaskManager(CollectorTaskManager collectorTaskManager) {
		this.collectorTaskManager = collectorTaskManager;
	}

	public boolean isAuthed() {
		return authed;
	}

	public void setAuthed(boolean authed) {
		this.authed = authed;
	}
	
	

}
