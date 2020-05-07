package com.ggx.router.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.client.GGClient;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.listener.RouterServiceShutdownListener;
import com.ggx.session.group.client.SessionGroupClient;
import com.ggx.session.group.client.config.SessionGroupClientConfig;

/**
 * 默认路由服务
 * 
 * @author zai
 * 2019-11-07 16:52:05
 */
public class DefaultRouterService implements RouterService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRouterService.class);
	
	protected RouterClientConfig config;
	
	protected String serviceId;
	
	protected String servcieName;
	
	protected String host;
	
	protected int port;
	
	protected TaskExecutor executor;
	
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
	 * 服务关闭监听器
	 */
	protected List<RouterServiceShutdownListener> shutdownListeners = new ArrayList<>();
	
	
	/**
	 * 是否已关闭
	 */
	protected boolean shutdown;
	
	/**
	 * 负载量
	 */
	protected AtomicInteger load = new AtomicInteger(0);
	
	

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
	public GGFuture dispatch(Pack pack) {
		if (!isAvailable()) {
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		if (isShutdown()) {
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		SessionManager sessionManager = this.serviceClient.getSessionManager();
		GGSession session = sessionManager.randomGetSession();
		if (session != null) {
			return session.send(pack);
		}
		
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}
	

	@Override
	public boolean isAvailable() {
		return this.sessionGroupClient.getAvaliableConnections() > 0;
	}

	
	/**
	 * 关闭
	 * 
	 * @author zai
	 * 2019-11-07 15:51:04
	 */
	@Override
	public void shutdown() {
		if (this.shutdown) {
			return;
		}
		this.shutdown = true;
		this.sessionGroupClient.shutdown(false);
		for (RouterServiceShutdownListener listener : shutdownListeners) {
			try {
				listener.onShutdown(this);
			} catch (Exception e) {
				GGLoggerUtil.getLogger(this).error("RouterServiceShutdownListener ERROR!", e);
			}
			
		}
	}
	
	@Override
	public void addShutdownListener(RouterServiceShutdownListener listener) {
		this.shutdownListeners.add(listener);
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
	public String getServcieName() {
		return servcieName;
	}
	
	public void setServcieName(String servcieName) {
		this.servcieName = servcieName;
	}
	
	public AtomicInteger getLoad() {
		return load;
	}


}
