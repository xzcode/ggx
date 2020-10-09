package com.ggx.server.starter.basic;

import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.GGXSuccessFuture;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.eventbus.client.subscriber.Subscriber;
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
import com.ggx.rpc.client.RpcClient;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.server.RpcServer;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.server.starter.GGXServerStarter;
import com.ggx.util.logger.GGXLogUtil;

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
	
	protected RpcClient rpcClient;
	protected RpcClientConfig rpcClientConfig;
	
	protected RpcServer rpcServer;
	protected RpcServerConfig rpcServerConfig;
	
	@Override
	public GGXFuture shutdown() {
		try {

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
			
			if (this.rpcClient != null) {
				this.rpcClient.shutdown();
			}
			
			if (this.rpcServer != null) {
				this.rpcServer.shutdown();
			}
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Shutdown ERROR!", e);
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		
		return GGXSuccessFuture.DEFAULT_SUCCESS_FUTURE;
		
	}
	
	
	
	
	@Override
	public void subscribe(String eventId, Subscriber subscriber) {
		if (this.eventbusGroupClient != null) {
			this.eventbusGroupClient.subscribe(eventId, subscriber);
		}
		
	}

	@Override
	public void publish(String eventId, Object data) {
		if (this.eventbusGroupClient != null) {
			this.eventbusGroupClient.publishEvent(eventId, data);
		}
		
	}
	
	

	@Override
	public void registerRpcService(Class<?> serviceInterface, Object serviceObj) {
		if (this.rpcServer != null) {
			this.rpcServer.register(serviceInterface, serviceObj);			
		}
	}

	@Override
	public Object registerRpcClient(Class<?> serviceInterface, Object fallbackObj) {
		if (this.rpcClient != null) {
			return this.rpcClient.register(serviceInterface, fallbackObj);			
		}
		return null;
	}

	public RegistryServer getRegistryServer() {
		return registryServer;
	}

	public void setRegistryServer(RegistryServer registryServer) {
		this.registryServer = registryServer;
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

	public RegistryClient getRegistryClient() {
		return registryClient;
	}

	public void setRegistryClient(RegistryClient registryClient) {
		this.registryClient = registryClient;
	}

	public EventbusServer getEventbusServer() {
		return eventbusServer;
	}

	public void setEventbusServer(EventbusServer eventbusServer) {
		this.eventbusServer = eventbusServer;
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

	public EventbusGroupClient getEventbusGroupClient() {
		return eventbusGroupClient;
	}

	public void setEventbusGroupClient(EventbusGroupClient eventbusGroupClient) {
		this.eventbusGroupClient = eventbusGroupClient;
	}

	public GGXCoreServer getCoreServer() {
		return coreServer;
	}

	public void setCoreServer(GGXCoreServer coreServer) {
		this.coreServer = coreServer;
	}

	public GGXCoreServerConfig getCoreServerConfig() {
		return coreServerConfig;
	}

	public void setCoreServerConfig(GGXCoreServerConfig coreServerConfig) {
		this.coreServerConfig = coreServerConfig;
	}

	public RouterServer getRouterServer() {
		return routerServer;
	}

	public void setRouterServer(RouterServer routerServer) {
		this.routerServer = routerServer;
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

	public RouterClient getRouterClient() {
		return routerClient;
	}

	public void setRouterClient(RouterClient routerClient) {
		this.routerClient = routerClient;
	}

	public RpcClient getRpcClient() {
		return rpcClient;
	}

	public void setRpcClient(RpcClient rpcClient) {
		this.rpcClient = rpcClient;
	}

	public RpcClientConfig getRpcClientConfig() {
		return rpcClientConfig;
	}

	public void setRpcClientConfig(RpcClientConfig rpcClientConfig) {
		this.rpcClientConfig = rpcClientConfig;
	}

	public RpcServer getRpcServer() {
		return rpcServer;
	}

	public void setRpcServer(RpcServer rpcServer) {
		this.rpcServer = rpcServer;
	}

	public RpcServerConfig getRpcServerConfig() {
		return rpcServerConfig;
	}

	public void setRpcServerConfig(RpcServerConfig rpcServerConfig) {
		this.rpcServerConfig = rpcServerConfig;
	}

	

	
}
