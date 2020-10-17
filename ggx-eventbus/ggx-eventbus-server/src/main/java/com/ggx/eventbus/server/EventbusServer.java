package com.ggx.eventbus.server;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.config.GGXCoreSupport;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.ggx.eventbus.server.controller.EventbusServerController;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;

public class EventbusServer implements GGXCoreSupport{
	
	private EventbusServerConfig config;
	private GGXCoreServer serviceServer;
	
	public EventbusServer(EventbusServerConfig config) {
		super();
		this.config = config;
	}

	public GGXFuture start() {
		SessionGroupServerConfig sessionServerConfig = new SessionGroupServerConfig();
		sessionServerConfig.setAuthToken(this.config.getAuthToken());
		sessionServerConfig.setPort(this.config.getPort());
		sessionServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionServerConfig.setWorkThreadFactory(new GGXThreadFactory("ggx-evt-serv-", false));
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
		
		this.serviceServer.registerMessageController(new EventbusServerController(config));
		
		
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
	
	public GGXFuture shutdown() {
		return this.config.getSessionGroupServer().shutdown();
	}
	
	public void setConfig(EventbusServerConfig config) {
		this.config = config;
	}
	
	public EventbusServerConfig getConfig() {
		return config;
	}

	@Override
	public GGXCore getGGXCore() {
		return this;
	}


	
}
