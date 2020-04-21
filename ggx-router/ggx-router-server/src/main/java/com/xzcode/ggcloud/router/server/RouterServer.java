package com.xzcode.ggcloud.router.server;

import java.nio.charset.Charset;

import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.support.IExecutorSupport;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.FilterSupport;
import com.ggx.core.common.future.IGGFuture;
import com.ggx.core.common.handler.serializer.ISerializer;
import com.ggx.core.common.message.request.manager.ReceiveMessageManager;
import com.ggx.core.common.message.request.support.ReceiveMessageSupport;
import com.ggx.core.common.message.response.support.SendMessageSupport;
import com.ggx.core.common.session.manager.ISessionManager;
import com.xzcode.ggcloud.discovery.client.DiscoveryClient;
import com.xzcode.ggcloud.router.common.constant.RouterServiceCustomDataKeys;
import com.xzcode.ggcloud.router.server.config.RouterServerConfig;
import com.xzcode.ggcloud.session.group.server.SessionGroupServer;
import com.xzcode.ggcloud.session.group.server.config.SessionGroupServerConfig;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

/**
 * 路由服务器对象
 * 
 * @author zai 2019-12-05 10:34:03
 */
public class RouterServer implements

		SendMessageSupport, ReceiveMessageSupport, FilterSupport, IExecutorSupport, EventSupport {

	private RouterServerConfig config;

	private GGServer serviceServer;

	public RouterServer(RouterServerConfig config) {

		this.config = config;

		DiscoveryClient discoveryClient = config.getDiscoveryClient();
		if (discoveryClient != null) {
			discoveryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_GROUP,config.getRouterGroupId());
			discoveryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX,config.getActionIdPrefix());
			discoveryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_PORT, String.valueOf(config.getPort()));
		}
	}

	public void init() {

		SessionGroupServerConfig sessionGroupServerConfig = new SessionGroupServerConfig();
		sessionGroupServerConfig.setAuthToken(this.config.getAuthToken());
		sessionGroupServerConfig.setEnableServiceServer(true);
		sessionGroupServerConfig.setPort(this.config.getPort());
		sessionGroupServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionGroupServerConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionGroupServerConfig.setWorkThreadFactory(new GGThreadFactory("gg-evt-serv-", false));

		if (this.config.getSharedEventLoopGroup() != null) {
			sessionGroupServerConfig.setWorkEventLoopGroup(this.config.getSharedEventLoopGroup());

		}

		SessionGroupServer sessionGroupServer = new SessionGroupServer(sessionGroupServerConfig);
		this.config.setSessionGroupServer(sessionGroupServer);

		this.serviceServer = sessionGroupServerConfig.getServiceServer();

	}

	public GGServer getServiceServer() {
		return serviceServer;
	}

	public RouterServerConfig getConfig() {
		return config;
	}

	public IGGFuture start() {
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
	public ReceiveMessageManager getRequestMessageManager() {
		return this.getServiceServerConfig().getRequestMessageManager();
	}

	@Override
	public ISessionManager getSessionManager() {
		return this.getServiceServerConfig().getSessionManager();
	}

	@Override
	public FilterManager getFilterManager() {
		return this.getServiceServerConfig().getFilterManager();
	}

}
