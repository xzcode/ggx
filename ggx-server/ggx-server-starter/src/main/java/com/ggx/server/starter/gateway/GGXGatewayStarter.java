package com.ggx.server.starter.gateway;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.config.GGXCore;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.core.server.impl.GGXDefaultCoreServer;
import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.rpc.client.RpcClient;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.server.RpcServer;
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
		}
		this.routerClientConfig.setRegistryClient(registryClient);
		this.routerClientConfig.setHostServer(coreServer);
		this.routerClient = new RouterClient(routerClientConfig);
		
		if (this.rpcClientConfig == null) {
			this.rpcClientConfig = new RpcClientConfig();
		}
		this.rpcClientConfig.setRegistryClient(registryClient);
		this.rpcClient = new RpcClient(rpcClientConfig);
		
		if (this.rpcServerConfig != null) {
			this.rpcServer = new RpcServer(rpcServerConfig);
			this.rpcServerConfig.setRegistryClient(registryClient);
		}
		
	}
	

	public void start() {
		GGXFuture<?> future = this.coreServer.start();
		future.addListener(f -> {
			if (f.isSuccess()) {
				this.rpcServer.start();
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
	
}
