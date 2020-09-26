package com.ggx.server.starter.service;

import com.ggx.core.common.config.GGXCore;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.server.RouterServer;
import com.ggx.router.server.config.RouterServerConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXServiceStarter extends GGXBasicServerStarter {


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

		if (this.routerServerConfig == null) {
			this.routerServerConfig = new RouterServerConfig();
		}
		this.routerServerConfig.setRegistryClient(registryClient);
		this.routerServer = new RouterServer(routerServerConfig);

	}

	@Override
	public void start() {

		this.routerServer.start().addListener(f -> {
			if (f.isSuccess()) {
				this.registryClient.start();
			} else {
				this.shutdown();
			}
		});
	}
	
	@Override
	public void subscribe(String eventId, Subscriber subscriber) {
		this.eventbusGroupClient.subscribe(eventId, subscriber);
	}

	@Override
	public GGXCore getGGXCore() {
		return this.routerServer;
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

	public EventbusGroupClientConfig getEventbusGroupClientConfig() {
		return eventbusGroupClientConfig;
	}

	public void setEventbusGroupClientConfig(EventbusGroupClientConfig eventbusGroupClientConfig) {
		this.eventbusGroupClientConfig = eventbusGroupClientConfig;
	}

	public RouterServerConfig getRouterServerConfig() {
		return routerServerConfig;
	}

	public void setRouterServerConfig(RouterServerConfig routerServerConfig) {
		this.routerServerConfig = routerServerConfig;
	}

	public RouterClientConfig getRouterClientConfig() {
		return routerClientConfig;
	}

	public void setRouterClientConfig(RouterClientConfig routerClientConfig) {
		this.routerClientConfig = routerClientConfig;
	}

}
