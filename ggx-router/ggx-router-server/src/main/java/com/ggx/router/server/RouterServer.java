package com.ggx.router.server;

import java.nio.charset.Charset;

import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.FilterSupport;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.handler.serializer.ISerializer;
import com.ggx.core.common.message.request.manager.ReceiveMessageManager;
import com.ggx.core.common.message.request.support.ReceiveMessageSupport;
import com.ggx.core.common.message.response.support.SendMessageSupport;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.impl.DefaultRouterClient;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;
import com.ggx.router.server.config.RouterServerConfig;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

/**
 * 路由服务器对象
 * 
 * @author zai 2019-12-05 10:34:03
 */
public class RouterServer implements

		SendMessageSupport, ReceiveMessageSupport, FilterSupport, ExecutorSupport, EventSupport {

	private RouterServerConfig config;

	private GGServer serviceServer;

	public RouterServer(RouterServerConfig config) {

		this.config = config;

		RegistryClient registryClient = config.getRegistryClient();
		if (registryClient != null) {
			if (config.getRouterGroupId() != null) {
				registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_GROUP_ID,config.getRouterGroupId());
			}
			if (config.getActionIdPrefix() != null) {
				registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX,config.getActionIdPrefix());
			}
			registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_PORT, String.valueOf(config.getPort()));
		}
		
		init();
	}

	public void init() {

		SessionGroupServerConfig sessionGroupServerConfig = new SessionGroupServerConfig();
		sessionGroupServerConfig.setAuthToken(this.config.getAuthToken());
		sessionGroupServerConfig.setEnableServiceServer(true);
		sessionGroupServerConfig.setPort(this.config.getPort());
		sessionGroupServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionGroupServerConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionGroupServerConfig.setWorkThreadFactory(new GGThreadFactory("gg-router-serv-", false));

		if (this.config.getSharedEventLoopGroup() != null) {
			sessionGroupServerConfig.setWorkEventLoopGroup(this.config.getSharedEventLoopGroup());

		}

		SessionGroupServer sessionGroupServer = new SessionGroupServer(sessionGroupServerConfig);
		this.config.setSessionGroupServer(sessionGroupServer);

		this.serviceServer = sessionGroupServerConfig.getServiceServer();
		
		//是否开启转发
		if (this.config.isEnableForwardRouterClient()) {
			RouterClientConfig routerClientConfig = new RouterClientConfig(this.serviceServer);
			
			
			routerClientConfig.setRouterGroupId(this.config.getForwardRouterClientGroupId());
			routerClientConfig.setServerHost(this.config.getForwardHost());
			routerClientConfig.setServerPort(this.config.getForwardPort());
			
			RouterClient forwardRouterClient = new DefaultRouterClient(routerClientConfig);
			this.config.setForwardRouterClient(forwardRouterClient);
			
			
			
			
		}

	}

	public GGServer getServiceServer() {
		return serviceServer;
	}

	public RouterServerConfig getConfig() {
		return config;
	}

	public GGFuture start() {
		return this.config.getSessionGroupServer().start();
	}

	@Override
	public Charset getCharset() {
		return null;
	}

	private GGServerConfig getServiceServerConfig() {
		return this.serviceServer.getConfig();
	}

	@Override
	public ISerializer getSerializer() {
		return this.getServiceServerConfig().getSerializer();
	}

	@Override
	public EventManager getEventManagerImpl() {
		return null;
	}

	@Override
	public TaskExecutor getTaskExecutor() {
		return this.getServiceServerConfig().getTaskExecutor();
	}

	@Override
	public ReceiveMessageManager getReceiveMessageManager() {
		return this.getServiceServerConfig().getReceiveMessageManager();
	}

	@Override
	public SessionManager getSessionManager() {
		return this.getServiceServerConfig().getSessionManager();
	}

	@Override
	public FilterManager getFilterManager() {
		return this.getServiceServerConfig().getFilterManager();
	}

}
