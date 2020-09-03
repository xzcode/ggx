package com.ggx.server.starter.eventbus;

import com.ggx.core.common.config.GGXCore;
import com.ggx.eventbus.server.EventbusServer;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXEventbusServerStarter extends GGXBasicServerStarter{
	
	@Override
	public void start() {
		
		if (this.registryClientConfig == null) {
			this.registryClientConfig = new RegistryClientConfig();
		}
		this.registryClient = new RegistryClient(registryClientConfig);
		
		
		if (this.eventbusServerConfig == null) {
			this.eventbusServerConfig = new EventbusServerConfig();
		}
		this.eventbusServer = new EventbusServer(eventbusServerConfig);
		
		this.eventbusServer.start().addListener(f -> {
			if (f.isSuccess()) {
				this.registryClient.start();
			}else {
				this.shutdown();
			}
		});
		
	}
	
	
	@Override
	public GGXCore getGGXCore() {
		return this.eventbusServer;
	}

	public RegistryClientConfig getRegistryClientConfig() {
		return registryClientConfig;
	}

	public void setRegistryClientConfig(RegistryClientConfig registryClientConfig) {
		this.registryClientConfig = registryClientConfig;
	}

	public EventbusServerConfig getEventbusServerConfig() {
		return eventbusServerConfig;
	}

	public void setEventbusServerConfig(EventbusServerConfig eventbusServerConfig) {
		this.eventbusServerConfig = eventbusServerConfig;
	}


}
