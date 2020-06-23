package com.ggx.registry.server.config;

import com.ggx.core.common.utils.GGXIdUtil;
import com.ggx.registry.common.constant.RegistryConstant;
import com.ggx.registry.common.service.ServiceManager;
import com.xzcode.ggserver.core.server.GGServer;

/**
 * 配置
 * 
 * 
 * @author zai
 * 2019-10-04 17:23:47
 */
public class RegistryServerConfig {
	
	//ggserver对象
	private GGServer server;
	
	//是否打印pingpong包信息
	protected boolean 	printPingPongInfo = false;
	
	//是否打印注册中心信息
	protected boolean 	showRegistryLog = false;
	
	
	//是否周期性打印服务信息
	protected boolean 	periodPrintServiceInfos = false;
	
	//服务管理器
	private ServiceManager serviceManager = new ServiceManager();
	
	//服务端口
	private int port = 19394;
	
	//服务地址
	private String host = "127.0.0.1";
	
	/**
	 * 服务组id
	 */
	private String serviceGroupId = RegistryConstant.DEFAULT_REGISTRY_GROUP_ID;
	
	//服务id
	private String serviceId = GGXIdUtil.newRandomStringId24();
	
	//认证token
	private String authToken = RegistryConstant.DEFAULT_AUTH_TOKEN;
	
	
	//客户端汇报周期(毫秒)
	private long clientReportInterval = 30L * 1000L;
	
	//服务失效时间(毫秒)
	private long serviceTimeoutDelay;
	
	//所在地区
	private String region = "default";
	
	//所在分区
	private String zone = "default";
	
	// 工作线程数
	protected int workThreadSize = 8;
	
	/**
	 * 是否把注册中心自身作为进行注册
	 */
	protected boolean registerSelf = true;


	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public long getClientReportInterval() {
		return clientReportInterval;
	}

	public void setClientReportInterval(long clientReportTimeout) {
		this.clientReportInterval = clientReportTimeout;
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
	
	public ServiceManager getServiceManager() {
		return serviceManager;
	}
	
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
	public long getServiceTimeoutDelay() {
		return serviceTimeoutDelay;
	}
	
	public void setServiceTimeoutDelay(long serviceTimeout) {
		this.serviceTimeoutDelay = serviceTimeout;
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

	public boolean isShowRegistryLog() {
		return showRegistryLog;
	}

	public void setShowRegistryLog(boolean showRegistryLog) {
		this.showRegistryLog = showRegistryLog;
	}
	
	public boolean isPeriodPrintServiceInfos() {
		return periodPrintServiceInfos;
	}
	
	public void setPeriodPrintServiceInfos(boolean periodPrintServiceInfos) {
		this.periodPrintServiceInfos = periodPrintServiceInfos;
	}
	
	public boolean isRegisterSelf() {
		return registerSelf;
	}
	
	public void setRegisterSelf(boolean registerSelf) {
		this.registerSelf = registerSelf;
	}
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public String getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	

}
