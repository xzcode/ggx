package com.ggx.server.starter.basic;

import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.eventbus.server.EventbusServer;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.server.RegistryServer;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.server.RouterServer;
import com.ggx.router.server.config.RouterServerConfig;
import com.ggx.server.starter.GGXServerStarter;
import com.xzcode.ggserver.core.server.GGXCoreServer;
import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;

public abstract class GGXBasicServerStarter implements GGXServerStarter{
	
	protected RegistryServer registryServer;
	protected RegistryServerConfig registryServerConfig;
	
	protected RegistryClientConfig registryClientConfig;
	protected RegistryClient registryClient;
	
	protected EventbusServer eventbusServer;
	protected EventbusServerConfig eventbusServerConfig;
	
	protected EventbusGroupClientConfig eventbusGroupClientConfig;
	protected EventbusGroupClient eventbusGroupClient;
	
	protected GGXCoreServer coreServer;
	protected GGXCoreServerConfig coreServerConfig;
	
	protected RouterServer routerServer;
	protected RouterServerConfig routerServerConfig;
	
	protected RouterClientConfig routerClientConfig;
	protected RouterClient routerClient;
	
	@Override
	public void shutdown() {
		if (this.registryClient != null) {
			this.registryClient.shutdown();
		}
		
		if (this.eventbusServer != null) {
			this.eventbusServer.shutdown();
		}
		
		if (this.eventbusGroupClient != null) {
			this.eventbusGroupClient.shutdown();
		}
		
		if (this.coreServer != null) {
			this.coreServer.shutdown();
		}
		
		if (this.routerServer != null) {
			this.routerServer.shutdown();
		}
		
		if (this.routerClient != null) {
			this.routerClient.shutdown();
		}
		
	}

	public RegistryServerConfig getRegistryServerConfig() {
		return registryServerConfig;
	}

	public void setRegistryServerConfig(RegistryServerConfig registryServerConfig) {
		this.registryServerConfig = registryServerConfig;
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

	public GGXCoreServerConfig getCoreServerConfig() {
		return coreServerConfig;
	}

	public void setCoreServerConfig(GGXCoreServerConfig coreServerConfig) {
		this.coreServerConfig = coreServerConfig;
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
