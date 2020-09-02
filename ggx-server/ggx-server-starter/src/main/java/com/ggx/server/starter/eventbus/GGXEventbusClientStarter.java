package com.ggx.server.starter.eventbus;

import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXEventbusClientStarter extends GGXBasicServerStarter{
	
	
	
	@Override
	public void start() {
		
		if (this.registryClientConfig == null) {
			this.registryClientConfig = new RegistryClientConfig();
		}
		this.registryClient = new RegistryClient(registryClientConfig);
		
		if (this.eventbusGroupClientConfig == null) {
			this.eventbusGroupClientConfig = new EventbusGroupClientConfig();
		}
		this.eventbusGroupClientConfig.setRegistryClient(registryClient);
		
		this.eventbusGroupClient = new EventbusGroupClient(eventbusGroupClientConfig);
		
		
		this.registryClient.start();
		
		
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


}
