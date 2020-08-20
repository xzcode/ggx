package com.ggx.eventbus.server;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.common.message.req.EventPublishReq;
import com.ggx.common.message.req.EventSubscribeReq;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.future.GGFuture;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.ggx.eventbus.server.handler.EventPublishReqHandler;
import com.ggx.eventbus.server.handler.EventSubscribeReqHandler;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.xzcode.ggserver.core.server.GGServer;

public class EventbusServer {
	
	private EventbusServerConfig config;
	
	public EventbusServer(EventbusServerConfig config) {
		super();
		this.config = config;
	}

	public GGFuture start() {
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
		
		GGServer serviceServer = sessionServerConfig.getServiceServer();
		
		serviceServer.onMessage(EventPublishReq.ACTION_ID, new EventPublishReqHandler(config));
		serviceServer.onMessage(EventSubscribeReq.ACTION_ID, new EventSubscribeReqHandler(config));
		
		
		GGFuture startFuture = sessionGroupServer.start();
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
	
	public void setConfig(EventbusServerConfig config) {
		this.config = config;
	}
	
	public EventbusServerConfig getConfig() {
		return config;
	}
	
}
