package com.ggx.eventbus.server;

import java.nio.charset.Charset;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.common.message.req.EventPublishReq;
import com.ggx.common.message.req.EventSubscribeReq;
import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.ggx.eventbus.server.handler.EventPublishReqHandler;
import com.ggx.eventbus.server.handler.EventSubscribeReqHandler;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;

public class EventbusServer implements GGXCore{
	
	private EventbusServerConfig config;
	private GGXCoreServer serviceServer;
	
	public EventbusServer(EventbusServerConfig config) {
		super();
		this.config = config;
	}

	public GGXFuture start() {
		SessionGroupServerConfig sessionServerConfig = new SessionGroupServerConfig();
		sessionServerConfig.setAuthToken(this.config.getAuthToken());
		sessionServerConfig.setEnableServiceServer(true);
		sessionServerConfig.setPort(this.config.getPort());
		sessionServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionServerConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionServerConfig.setWorkThreadFactory(new GGThreadFactory("gg-evt-serv-", false));
		sessionServerConfig.setPortChangeStrategy(this.config.getPortChangeStrategy());
		sessionServerConfig.setChangeAndRebootIfPortInUse(this.config.isChangeAndRebootIfPortInUse());
		sessionServerConfig.setBootWithRandomPort(this.config.isBootWithRandomPort());
		
		
		SessionGroupServer sessionGroupServer = new SessionGroupServer(sessionServerConfig);
		this.config.setSessionGroupServer(sessionGroupServer);
		
		//包日志输出控制
		if (this.config.isPrintEventbusPackLog()) {
			sessionServerConfig.getSessionServer().getConfig().getPackLogger().addPackLogFilter(pack -> {
				String actionString = pack.getActionString();
				return !(actionString.startsWith(EventbusConstant.ACTION_ID_PREFIX));
			});
		}
		
		this.serviceServer = sessionServerConfig.getServiceServer();
		
		this.serviceServer.onMessage(EventPublishReq.ACTION_ID, new EventPublishReqHandler(config));
		this.serviceServer.onMessage(EventSubscribeReq.ACTION_ID, new EventSubscribeReqHandler(config));
		
		
		GGXFuture startFuture = sessionGroupServer.start();
		startFuture.addListener(f -> {
			if (f.isSuccess()) {
				//获取注册中心客户端
				RegistryClient registryClient = this.config.getRegistryClient();
				if (registryClient != null) {
					//添加自定义参数
					//添加自定义事件组id
					registryClient.addCustomData(EventbusConstant.REGISTRY_CUSTOM_EVENTBUS_GROUP_KEY, this.config.getEventbusGroupId());
					//添加自定义事件服务端端口
					registryClient.addCustomData(EventbusConstant.REGISTRY_CUSTOM_EVENTBUS_PORT_KEY, String.valueOf(this.config.getSessionGroupServer().getConfig().getSessionServer().getConfig().getPort()));
				}
			}
		});
		
		return startFuture;
	}
	
	public void shutdown() {
		this.config.getSessionGroupServer().shutdown();
	}
	
	public void setConfig(EventbusServerConfig config) {
		this.config = config;
	}
	
	public EventbusServerConfig getConfig() {
		return config;
	}

	@Override
	public SessionManager getSessionManager() {
		return null;
	}

	@Override
	public FilterManager getFilterManager() {
		return this.serviceServer ;
	}

	@Override
	public Charset getCharset() {
		return this.serviceServer.getCharset();
	}

	@Override
	public Serializer getSerializer() {
		return this.serviceServer.getSerializer();
	}

	@Override
	public ReceiveMessageManager getReceiveMessageManager() {
		return this.serviceServer.getReceiveMessageManager();
	}

	@Override
	public TaskExecutor getTaskExecutor() {
		return this.serviceServer.getTaskExecutor();
	}

	@Override
	public EventManager getEventManager() {
		return this.serviceServer.getEventManager();
	}
	
}
