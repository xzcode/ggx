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
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.message.receive.support.ReceiveMessageSupport;
import com.ggx.core.common.message.send.support.SendMessageSupport;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;
import com.ggx.router.server.config.RouterServerConfig;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 路由服务器对象
 * 
 * @author zai 2019-12-05 10:34:03
 */
public class RouterServer implements SendMessageSupport, ReceiveMessageSupport, FilterSupport, ExecutorSupport, EventSupport {

	private RouterServerConfig config;
	
	
	protected GGServer serviceServer;


	public RouterServer(RouterServerConfig config) {

		this.config = config;

		init();
	}

	public void init() {
		
		if (this.config.getSharedEventLoopGroup() == null) {
			this.config.setSharedEventLoopGroup(new NioEventLoopGroup(this.config.getWorkThreadSize(), new GGThreadFactory("gg-router-serv-", false)));
		}

		SessionGroupServerConfig sessionServerConfig = new SessionGroupServerConfig();
		sessionServerConfig.setAuthToken(this.config.getAuthToken());
		sessionServerConfig.setEnableServiceServer(true);
		sessionServerConfig.setPort(this.config.getPort());
		sessionServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionServerConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		//sessionGroupServerConfig.setWorkThreadFactory(new GGThreadFactory("gg-router-serv-", false));
		sessionServerConfig.setPortChangeStrategy(this.config.getPortChangeStrategy());
		sessionServerConfig.setChangeAndRebootIfPortInUse(this.config.isChangeAndRebootIfPortInUse());
		sessionServerConfig.setBootWithRandomPort(this.config.isBootWithRandomPort());
		
		
		if (this.config.getSharedEventLoopGroup() != null) {
			sessionServerConfig.setWorkEventLoopGroup(this.config.getSharedEventLoopGroup());

		}

		SessionGroupServer sessionServer = new SessionGroupServer(sessionServerConfig);
		this.config.setSessionGroupServer(sessionServer);
		
		
		this.serviceServer = sessionServerConfig.getServiceServer();

	}

	public GGServer getServiceServer() {
		return this.serviceServer;
	}

	public RouterServerConfig getConfig() {
		return config;
	}

	public GGFuture start() {
		GGFuture startFuture = this.config.getSessionGroupServer().start();
		startFuture.addListener(f -> {
			if (f.isSuccess()) {
				
				RegistryClient registryClient = config.getRegistryClient();
				if (registryClient != null) {
					if (config.getRouterGroupId() != null) {
						registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_GROUP_ID,config.getRouterGroupId());
					}
					if (config.getActionIdPrefix() != null) {
						registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX,config.getActionIdPrefix());
					}
					registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_PORT, String.valueOf(this.config.getSessionGroupServer().getConfig().getSessionServer().getConfig().getPort()));
				}
				
			}
		});
		
		return startFuture;
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
		return this.getServiceServerConfig().getEventManager();
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
