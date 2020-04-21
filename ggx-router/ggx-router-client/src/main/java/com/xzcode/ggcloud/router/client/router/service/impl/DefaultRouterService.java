package com.xzcode.ggcloud.router.client.router.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.client.GGClient;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.IGGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.manager.ISessionManager;
import com.xzcode.ggcloud.router.client.config.RouterClientConfig;
import com.xzcode.ggcloud.router.client.event.RouterClientEvents;
import com.xzcode.ggcloud.router.client.router.service.IRouterService;
import com.xzcode.ggcloud.router.client.router.service.IRouterServiceMatcher;
import com.xzcode.ggcloud.router.client.router.service.listener.IRouterServiceActiveListener;
import com.xzcode.ggcloud.router.client.router.service.listener.IRouterServiceInActiveListener;
import com.xzcode.ggcloud.session.group.client.SessionGroupClient;
import com.xzcode.ggcloud.session.group.client.config.SessionGroupClientConfig;
import com.xzcode.ggcloud.session.group.common.constant.GGSessionGroupEventConstant;

/**
 * 默认路由服务
 * 
 * @author zai
 * 2019-11-07 16:52:05
 */
public class DefaultRouterService implements IRouterService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRouterService.class);
	
	protected RouterClientConfig config;
	
	protected String serviceId;
	
	protected String servcieName;
	
	protected String host;
	
	protected int port;
	
	protected IRouterServiceMatcher serviceMatcher;
	
	
	protected TaskExecutor executor;
	
	
	protected IGGFuture checkConnectionsFuture;
	
	/**
	 * 绑定的连接客户端
	 */
	protected SessionGroupClient sessionGroupClient;
	
	
	protected GGClient serviceClient;
	
	
	/**
	 * 是否已准备接收数据
	 */
	protected AtomicInteger avaliableConnections = new AtomicInteger(0);
	/**
	 * 额外数据
	 */
	protected Map<String, String> customData = new ConcurrentHashMap<>();
	
	/**
	 * 服务生效监听
	 */
	protected List<IRouterServiceActiveListener> activeListeners = new ArrayList<>();
	
	/**
	 * 服务失效监听器
	 */
	protected List<IRouterServiceInActiveListener> inActiveListeners = new ArrayList<>();
	
	/**
	 * 是否已关闭
	 */
	protected boolean shutdown;
	

	public DefaultRouterService(RouterClientConfig config, String serviceId) {
		this.config = config;
		this.serviceId = serviceId;
	}
	
	/**
	 * 初始化
	 * 
	 * @author zai
	 * 2019-11-07 15:50:25
	 */
	public void init() {
		
		SessionGroupClientConfig sessionGroupClientConfig = new SessionGroupClientConfig();
		sessionGroupClientConfig.setEnableServiceClient(true);
		sessionGroupClientConfig.setAuthToken(this.config.getAuthToken());
		sessionGroupClientConfig.setWorkThreadFactory(new GGThreadFactory("gg-router-cli-", false));
		sessionGroupClientConfig.setConnectionSize(this.config.getConnectionSize());
		sessionGroupClientConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionGroupClientConfig.setServerHost(this.host);
		sessionGroupClientConfig.setServerPort(this.port);
		
		
		SessionGroupClient sessionGroupClient = new SessionGroupClient(sessionGroupClientConfig);
		
		//添加会话注册成功监听
		sessionGroupClient.addEventListener(GGSessionGroupEventConstant.SESSION_REGISTER_SUCCESS, e -> {
			
			
			
		});
		
		this.serviceClient = sessionGroupClientConfig.getServiceClient();
		
		this.sessionGroupClient = sessionGroupClient;
		
		
		//包日志输出控制
		if (!this.config.isPrintRouterInfo()) {
			this.serviceClient.getConfig().getPackLogger().addPackLogFilter(pack -> {
				return false;
			});
		}
		
		
		sessionGroupClient.start();
		
	}

	
	/**
	 * 转发消息
	 * 
	 * @param pack
	 * @author zai
	 * 2019-11-07 17:53:00
	 */
	public IGGFuture dispatch(Pack pack) {
		if (!isAvailable()) {
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		if (isShutdown()) {
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		GGClient serviceClient = this.sessionGroupClient.getConfig().getServiceClient();
		
		return serviceClient.send(pack);
	}
	
	/**
	 * 关闭
	 * 
	 * @author zai
	 * 2019-11-07 15:51:04
	 */
	@Override
	public void shutdown() {
		this.shutdown = true;
		ISessionManager sessionManager = this.serviceClient.getSessionManager();
		sessionManager.disconnectAllSession();
		config.getRoutingServer().emitEvent(new EventData<IRouterService>(RouterClientEvents.RouterService.SHUTDOWN, this));
	}
	
	
	public void addActiveListener(IRouterServiceActiveListener listener) {
		this.activeListeners.add(listener);
	}
	
	public void addInActiveListener(IRouterServiceInActiveListener listener) {
		this.inActiveListeners.add(listener);
	}

	
	public void setServiceMatcher(IRouterServiceMatcher serviceMatcher) {
		this.serviceMatcher = serviceMatcher;
	}
	

	@Override
	public IRouterServiceMatcher getServiceMatcher() {
		return this.serviceMatcher;
	}
	
	@Override
	public String getServiceId() {
		return this.serviceId;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public int getPort() {
		return port;
	}
	

	@Override
	public String getExtraData(String key) {
		return this.customData.get(key);
	}
	
	public void removeExtraData(String key) {
		this.customData.remove(key);
	}
	public void addExtraData(String key, String data) {
		this.customData.put(key, data);
	}
	public void addAllExtraData(Map<String, String> extraData) {
		this.customData.putAll(extraData);
	}
	public void replaceExtraData(Map<String, String> extraData) {
		this.customData.clear();
		this.customData.putAll(extraData);
	}
	
	public Map<String, String> getExtraDatas() {
		return customData;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
	public boolean isShutdown() {
		return shutdown;
	}
	public IGGFuture getCheckConnectionsFuture() {
		return checkConnectionsFuture;
	}
	public String getServcieName() {
		return servcieName;
	}
	
	public void setServcieName(String servcieName) {
		this.servcieName = servcieName;
	}

	@Override
	public boolean isAvailable() {
		return true;
	}


}
