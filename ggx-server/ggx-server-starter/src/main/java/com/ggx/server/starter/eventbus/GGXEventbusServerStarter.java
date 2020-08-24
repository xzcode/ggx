package com.ggx.server.starter.eventbus;

import com.ggx.eventbus.server.EventbusServer;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.server.starter.GGXServerStarter;

public class GGXEventbusServerStarter implements GGXServerStarter{
	

	protected RegistryClient registryClient;
	protected RegistryClientConfig registryClientConfig;
	
	protected EventbusServer eventbusServer;
	protected EventbusServerConfig eventbusServerConfig;
	
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
		
	}

	@Override
	public void shutdown() {
		
	}

}
