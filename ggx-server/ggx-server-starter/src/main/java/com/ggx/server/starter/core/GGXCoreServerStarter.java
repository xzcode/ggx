package com.ggx.server.starter.core;

import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.router.server.RouterServer;
import com.ggx.router.server.config.RouterServerConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;
import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;

public class GGXCoreServerStarter extends GGXBasicServerStarter{
	
	

	@Override
	public void start() {
		
		if (this.eventbusGroupClientConfig == null) {
			this.eventbusGroupClientConfig = new EventbusGroupClientConfig();
		}
		this.eventbusGroupClient = new EventbusGroupClient(eventbusGroupClientConfig);
		
		if (this.routerServerConfig == null) {
			this.routerServerConfig = new RouterServerConfig();
		}
		this.routerServer = new RouterServer(routerServerConfig);
		
		
		if (this.registryClientConfig == null) {
			this.registryClientConfig = new RegistryClientConfig();
		}
		this.registryClient = new RegistryClient(registryClientConfig);
		
		this.routerServer.start().addListener(f -> {
			if (f.isSuccess()) {
				this.registryClient.start();
			}else {
				this.shutdown();
			}
		});
		
	}
	
	public RegistryClientConfig getRegistryClientConfig() {
		return registryClientConfig;
	}

	public void setRegistryClientConfig(RegistryClientConfig registryClientConfig) {
		this.registryClientConfig = registryClientConfig;
	}

	public EventbusGroupClientConfig getEventbusGroupClientConfig() {
		return eventbusGroupClientConfig;
	}

	public void setEventbusGroupClientConfig(EventbusGroupClientConfig eventbusGroupClientConfig) {
		this.eventbusGroupClientConfig = eventbusGroupClientConfig;
	}

	public GGXCoreServerConfig getCoreServerConfig() {
		return coreServerConfig;
	}

	public void setCoreServerConfig(GGXCoreServerConfig coreServerConfig) {
		this.coreServerConfig = coreServerConfig;
	}


}
