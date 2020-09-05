package com.ggx.server.starter.gateway;

import com.ggx.core.common.config.GGXCore;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.core.server.impl.GGXDefaultCoreServer;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXGatewayStarter  extends GGXBasicServerStarter{
	
	
	public void init() {
		if (this.registryClientConfig == null) {
			this.registryClientConfig = new RegistryClientConfig();
		}
		this.registryClient = new RegistryClient(registryClientConfig);
		
		if (this.eventbusGroupClientConfig == null) {
			this.eventbusGroupClientConfig = new EventbusGroupClientConfig();
		}
		this.eventbusGroupClientConfig.setRegistryClient(registryClient);
		this.eventbusGroupClient = new EventbusGroupClient(eventbusGroupClientConfig);
		this.eventbusGroupClient.start();
		
		if (this.coreServerConfig == null) {
			this.coreServerConfig = new GGXCoreServerConfig();
		}
		this.coreServer = new GGXDefaultCoreServer(coreServerConfig);
		
		if (this.routerClientConfig == null) {
			this.routerClientConfig = new RouterClientConfig(coreServer);
		}else {
			this.routerClientConfig.setHostServer(coreServer);
		}
		this.routerClient = new RouterClient(routerClientConfig);
	}
	
	@Override
	public void subscribe(String eventId, Subscriber<?> subscriber) {
		this.eventbusGroupClient.subscribe(eventId, subscriber);
	}
	
	
	public void start() {
		
		this.coreServer.start().addListener(f -> {
			if (f.isSuccess()) {
				this.registryClient.start();
			}else {
				this.shutdown();
			}
		});
		
	}
	
	@Override
	public GGXCore getGGXCore() {
		return this.coreServer;
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


	public RouterClientConfig getRouterClientConfig() {
		return routerClientConfig;
	}

	public void setRouterClientConfig(RouterClientConfig routerClientConfig) {
		this.routerClientConfig = routerClientConfig;
	}
	
	
}
