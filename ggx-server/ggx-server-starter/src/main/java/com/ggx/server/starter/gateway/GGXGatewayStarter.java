package com.ggx.server.starter.gateway;

import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;
import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;
import com.xzcode.ggserver.core.server.impl.GGXDefaultCoreServer;

public class GGXGatewayStarter  extends GGXBasicServerStarter{
	
	/**
	 * 启动
	 *
	 * @author zzz
	 * 2020-08-21 18:24:48
	 */
	public void start() {
		
		if (this.eventbusGroupClientConfig == null) {
			this.eventbusGroupClientConfig = new EventbusGroupClientConfig();
		}
		this.eventbusGroupClient = new EventbusGroupClient(eventbusGroupClientConfig);
		
		
		if (this.coreServerConfig == null) {
			this.coreServerConfig = new GGXCoreServerConfig();
		}
		this.coreServer = new GGXDefaultCoreServer(coreServerConfig);
		
		
		if (this.registryClientConfig == null) {
			this.registryClientConfig = new RegistryClientConfig();
		}
		this.registryClient = new RegistryClient(registryClientConfig);
		
		if (this.routerClientConfig == null) {
			this.routerClientConfig = new RouterClientConfig(coreServer);
		}
		this.routerClient = new RouterClient(routerClientConfig);
		
		
		this.coreServer.start().addListener(f -> {
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


	public RouterClientConfig getRouterClientConfig() {
		return routerClientConfig;
	}

	public void setRouterClientConfig(RouterClientConfig routerClientConfig) {
		this.routerClientConfig = routerClientConfig;
	}
	
	
}
