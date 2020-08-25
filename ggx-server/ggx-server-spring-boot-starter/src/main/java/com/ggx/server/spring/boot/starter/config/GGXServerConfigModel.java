package com.ggx.server.spring.boot.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;

@ConfigurationProperties(prefix = "ggx")
public class GGXServerConfigModel {
	
	protected GGXCoreServerConfig core;
	
	protected GGXServerEventbusConfigModel eventbus;
	
	protected GGXServerRegistryConfigModel registry;
	
	protected GGXServerRouterConfigModel router;
	
	
	public GGXCoreServerConfig getCore() {
		return core;
	}
	
	public void setCore(GGXCoreServerConfig core) {
		this.core = core;
	}

	public GGXServerEventbusConfigModel getEventbus() {
		return eventbus;
	}

	public void setEventbus(GGXServerEventbusConfigModel eventbus) {
		this.eventbus = eventbus;
	}

	public GGXServerRegistryConfigModel getRegistry() {
		return registry;
	}

	public void setRegistry(GGXServerRegistryConfigModel registry) {
		this.registry = registry;
	}

	public GGXServerRouterConfigModel getRouter() {
		return router;
	}

	public void setRouter(GGXServerRouterConfigModel router) {
		this.router = router;
	}

	
}
