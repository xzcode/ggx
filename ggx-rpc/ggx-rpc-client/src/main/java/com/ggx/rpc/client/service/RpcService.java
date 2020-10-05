package com.ggx.rpc.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.handler.RpcRespHandler;
import com.ggx.rpc.common.message.req.RpcReq;
import com.ggx.session.group.client.SessionGroupClient;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.manager.listener.Listener;

public class RpcService {

	protected RpcClientConfig config;

	protected String serviceId;

	protected String serviceGroupId;

	protected String actionIdPrefix;

	protected String serviceName;

	protected String host;

	protected int port;

	protected TaskExecutor executor;

	// 业务客户端
	protected GGXCoreClient serviceClient;

	// 绑定的连接客户端
	protected SessionGroupClient sessionGroupClient;

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
	protected List<Listener<RpcService>> shutdownListeners = new ArrayList<>();

	/**
	 * 是否已关闭
	 */
	protected boolean shutdown;

	public RpcService(RpcClientConfig config) {
		this.config = config;
	}

	/**
	 * 初始化
	 * 
	 * @author zai 2019-11-07 15:50:25
	 */
	public void init() {

		SessionGroupClientConfig sessionGroupClientConfig = new SessionGroupClientConfig();
		sessionGroupClientConfig.setEnableServiceClient(true);
		sessionGroupClientConfig.setWorkEventLoopGroup(this.config.getSharedEventLoopGroup());
		sessionGroupClientConfig.setAuthToken(this.config.getAuthToken());
		sessionGroupClientConfig.setConnectionSize(this.config.getConnectionSize());
		sessionGroupClientConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionGroupClientConfig.setServerHost(this.host);
		sessionGroupClientConfig.setServerPort(this.port);
		

		SessionGroupClient sessionGroupClient = new SessionGroupClient(sessionGroupClientConfig);

		this.sessionGroupClient = sessionGroupClient;

		this.serviceClient = sessionGroupClientConfig.getServiceClient();
		
		GGXCoreClientConfig serviceClientConfig = this.serviceClient.getConfig();
		serviceClientConfig.setGgxComponent(true);
		this.serviceClient.onMessage(new RpcRespHandler(config));

		this.executor = this.serviceClient.getTaskExecutor().nextEvecutor();
		
		sessionGroupClient.start();

	}

	/**
	 * 转发消息
	 * 
	 * @param pack
	 * @author zai 2019-11-07 17:53:00
	 */
	public GGXFuture invoke(RpcReq req) {
		if (!isAvailable()) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		if (isShutdown()) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		SessionManager serviceClientSessionManager = this.serviceClient.getSessionManager();
		GGXSession serviceClientSession = serviceClientSessionManager.randomGetSession();
		
		return serviceClientSession.send(req);

	}

	public boolean isAvailable() {
		if (shutdown) {
			return false;
		}
		if (this.sessionGroupClient != null) {
			return this.sessionGroupClient.getAvaliableConnections() > 0;
		}
		return false;
	}

	/**
	 * 关闭
	 * 
	 * @author zai 2019-11-07 15:51:04
	 */
	public void shutdown() {
		this.executor.submitTask(() -> {

			if (this.shutdown) {
				return;
			}
			this.shutdown = true;
			this.sessionGroupClient.shutdown(false);
			for (Listener<RpcService> listener : shutdownListeners) {
				try {
					listener.onTrigger(this);
				} catch (Exception e) {
					GGXLogUtil.getLogger(this).error(listener.getClass().getSimpleName() + " ERROR!", e);
				}

			}
		});
	}

	public void addShutdownListener(Listener<RpcService> listener) {
		this.executor.submitTask(() -> {
			if (shutdown) {
				listener.onTrigger(this);
				return;
			}
			this.shutdownListeners.add(listener);
		});
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

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

	public String getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(String servcieName) {
		this.serviceGroupId = servcieName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
