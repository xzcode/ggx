package com.ggx.registry.client.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.client.GGClient;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.GGXIdUtil;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.registry.RegistryInfo;
import com.ggx.registry.client.registry.RegistryManager;
import com.ggx.registry.common.constant.RegistryConstant;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 配置
 * 
 * 
 * @author zai
 * 2019-10-04 17:23:47
 */
public class RegistryClientConfig {
	
	//discoveryClient对象
	protected RegistryClient registryClient;
	
	protected boolean 	printPingPongInfo = false;
	
	//GGClient对象
	protected GGClient ggclient;
	
	//GGSession对象
	protected GGSession session;
	
	//是否打印注册中心信息
	protected boolean 	showRegistryLog = false;
	
	//是否打印pingpong包信息
	protected boolean 	pingPongEnabled = false;
	
	//服务管理器
	protected ServiceManager serviceManager = new ServiceManager();
	
	//注册中心信息
	protected List<RegistryInfo> registries = new ArrayList<>();
	
	//注册中心管理器
	protected RegistryManager registryManager = new RegistryManager(registries);
	
	//客户端汇报超时时间(秒)
	protected long clientReportInterval = 30L * 1000L;
	
	//重连间隔-秒
	protected long reconnectInterval = 5L * 1000L;
	
	//尝试重新注册周期，ms
	protected long tryRegisterInterval = 10L * 1000L;
	
	//验证token
	protected String authToken = RegistryConstant.DEFAULT_AUTH_TOKEN;

	//服务id
	protected String serviceId = GGXIdUtil.newRandomStringId24();
	
	//服务组id
	protected String serviceGroupId = "default_service_group";
	
	//服务名称
	protected String serviceName;
	
	//所在地区
	protected String region = "default";
		
	//所在分区
	protected String zone = "default";
	
	//同步服务信息
	protected Runnable syncServicesTask;
	
	protected GGFuture syncServicesTaskFuture;
	
	//是否需要获取注册中心的服务信息
	protected boolean requireServices = true;
	
	/**
	 * 自定义数据
	 */
	private Map<String, String> customData = new ConcurrentHashMap<>(6);
	
	/**
	 * 添加自定义参数
	 * 
	 * @param key
	 * @param value
	 * @author zai
	 * 2020-02-04 11:19:05
	 */
	public void addCustomData(String key, String value) {
		customData.put(key, value);
	}
	
	public Map<String, String> getCustomData() {
		return customData;
	}
	
	public void setCustomData(Map<String, String> extraData) {
		this.customData = extraData;
	}

	public GGClient getGGclient() {
		return ggclient;
	}

	public void setGGclient(GGClient ggclient) {
		this.ggclient = ggclient;
	}

	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	public List<RegistryInfo> getRegistries() {
		return registries;
	}

	public void setRegistries(List<RegistryInfo> registries) {
		this.registries = registries;
	}

	public RegistryManager getRegistryManager() {
		return registryManager;
	}

	public void setRegistryManager(RegistryManager registryManager) {
		this.registryManager = registryManager;
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
	
	public String getAuthToken() {
		return authToken;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getServiceGroupId() {
		return serviceGroupId;
	}
	
	public void setServiceGroupId(String serviceName) {
		this.serviceGroupId = serviceName;
	}
	
	public void setSession(GGSession session) {
		this.session = session;
	}
	public GGSession getSession() {
		return session;
	}
	
	public RegistryClient getRegistryClient() {
		return registryClient;
	}
	
	public void setRegistryClient(RegistryClient discoveryClient) {
		this.registryClient = discoveryClient;
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
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceName() {
		return serviceName;
	}

	public boolean isShowRegistryLog() {
		return showRegistryLog;
	}

	public void setShowRegistryLog(boolean showRegistryLog) {
		this.showRegistryLog = showRegistryLog;
	}
	
	public Runnable getSyncServicesTask() {
		return syncServicesTask;
	}
	
	public void setSyncServicesTask(Runnable syncServicesTask) {
		this.syncServicesTask = syncServicesTask;
	}
	
	public boolean isRequireServices() {
		return requireServices;
	}
	
	public void setRequireServices(boolean requireServices) {
		this.requireServices = requireServices;
	}
}
