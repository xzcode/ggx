package com.xzcode.ggcloud.eventbus.server;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.common.message.req.EventPublishReq;
import com.ggx.common.message.req.EventSubscribeReq;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.group.common.constant.GGSesssionGroupConstant;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.xzcode.ggcloud.eventbus.server.config.EventbusServerConfig;
import com.xzcode.ggcloud.eventbus.server.handler.EventPublishReqHandler;
import com.xzcode.ggcloud.eventbus.server.handler.EventSubscribeReqHandler;
import com.xzcode.ggserver.core.server.GGServer;

public class EventbusServer {
	
	private EventbusServerConfig config;
	
	public EventbusServer(EventbusServerConfig config) {
		super();
		this.config = config;
	}

	public void start() {
		SessionGroupServerConfig sessionGroupServerConfig = new SessionGroupServerConfig();
		sessionGroupServerConfig.setAuthToken(this.config.getAuthToken());
		sessionGroupServerConfig.setEnableServiceServer(true);
		sessionGroupServerConfig.setPort(this.config.getPort());
		sessionGroupServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionGroupServerConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionGroupServerConfig.setWorkThreadFactory(new GGThreadFactory("gg-evt-serv-", false));
		
		
		
		
		SessionGroupServer sessionGroupServer = new SessionGroupServer(sessionGroupServerConfig);
		this.config.setSessionGroupServer(sessionGroupServer);
		
		//包日志输出控制
		if (this.config.isPrintEventbusPackLog()) {
			sessionGroupServerConfig.getSessionServer().getConfig().getPackLogger().addPackLogFilter(pack -> {
				String actionString = pack.getActionString();
				return !(actionString.startsWith(EventbusConstant.ACTION_ID_PREFIX));
			});
		}
		
		GGServer serviceServer = sessionGroupServerConfig.getServiceServer();
		
		serviceServer.onMessage(EventPublishReq.ACTION_ID, new EventPublishReqHandler(config));
		serviceServer.onMessage(EventSubscribeReq.ACTION_ID, new EventSubscribeReqHandler(config));
		
		//获取注册中心客户端
		RegistryClient registryClient = this.config.getRegistryClient();
		if (registryClient != null) {
			//添加自定义参数
			//添加自定义事件组id
			registryClient.addCustomData(EventbusConstant.REGISTRY_CUSTOM_EVENTBUS_GROUP_KEY, this.config.getEventbusGroupId());
			//添加自定义事件服务端端口
			registryClient.addCustomData(EventbusConstant.REGISTRY_CUSTOM_EVENTBUS_PORT_KEY, String.valueOf(this.config.getPort()));
		}
		
		
		sessionGroupServer.start();
	}
	
	public void setConfig(EventbusServerConfig config) {
		this.config = config;
	}
	
	public EventbusServerConfig getConfig() {
		return config;
	}
	
}
