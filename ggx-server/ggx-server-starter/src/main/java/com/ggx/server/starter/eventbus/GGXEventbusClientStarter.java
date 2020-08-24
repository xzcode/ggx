package com.ggx.server.starter.eventbus;

import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXEventbusClientStarter extends GGXBasicServerStarter{
	
	protected EventbusGroupClientConfig eventbusGroupClientConfig;
	protected EventbusGroupClient eventbusGroupClient;
	
	
	protected RegistryClientConfig registryClientConfig;
	protected RegistryClient registryClient;
	
	
	@Override
	public void start() {
		
		if (this.eventbusGroupClientConfig == null) {
			this.eventbusGroupClientConfig = new EventbusGroupClientConfig();
		}
		this.eventbusGroupClient = new EventbusGroupClient(eventbusGroupClientConfig);

		
		if (this.registryClientConfig == null) {
			this.registryClientConfig = new RegistryClientConfig();
		}
		this.registryClient = new RegistryClient(registryClientConfig);
		
	}

}
