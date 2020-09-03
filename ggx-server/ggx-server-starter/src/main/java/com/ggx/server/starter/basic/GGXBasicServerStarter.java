package com.ggx.server.starter.basic;

import java.nio.charset.Charset;

import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
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
	
	public abstract GGXCore getGGXCore();
	
	@Override
	public SessionManager getSessionManager() {
		return getGGXCore().getSessionManager();
	}

	@Override
	public FilterManager getFilterManager() {
		return getGGXCore().getFilterManager();
	}

	@Override
	public Charset getCharset() {
		return getGGXCore().getCharset();
	}

	@Override
	public Serializer getSerializer() {
		return getGGXCore().getSerializer();
	}

	@Override
	public ReceiveMessageManager getReceiveMessageManager() {
		return getGGXCore().getReceiveMessageManager();
	}

	@Override
	public TaskExecutor getTaskExecutor() {
		return getGGXCore().getTaskExecutor();
	}

	@Override
	public EventManager getEventManager() {
		return getGGXCore().getEventManager();
	}

	

}
