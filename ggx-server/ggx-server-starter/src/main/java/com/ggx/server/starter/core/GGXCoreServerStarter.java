package com.ggx.server.starter.core;

import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.core.server.impl.GGXDefaultCoreServer;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.rpc.client.RpcClient;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXCoreServerStarter extends GGXBasicServerStarter{
	
	
	public void init() {
		if (this.registryClientConfig == null) {
			this.registryClientConfig = new RegistryClientConfig();
		}
		this.registryClient = new RegistryClient(registryClientConfig);
		
		if (this.eventbusGroupClientConfig == null) {
			this.eventbusGroupClientConfig = new EventbusGroupClientConfig();
		}
		this.eventbusGroupClientConfig.setRegistryClient(this.registryClient);
		
		this.eventbusGroupClient = new EventbusGroupClient(eventbusGroupClientConfig);
		this.eventbusGroupClient.start();
		
		if (this.coreServerConfig == null) {
			this.coreServerConfig = new GGXCoreServerConfig();
		}
		
		this.coreServer = new GGXDefaultCoreServer(this.coreServerConfig);
		
		if (this.rpcClientConfig == null) {
			this.rpcClientConfig = new RpcClientConfig();
		}
		this.rpcClientConfig.setRegistryClient(registryClient);
		this.rpcClient = new RpcClient(rpcClientConfig);
		
		
		
	}

	@Override
	public void start() {
		this.routerServer.start().addListener(f -> {
			if (f.isSuccess()) {
				this.registryClient.start();
			}else {
				this.shutdown();
			}
		});
		
	}


	@Override
	public GGXCore getGGXCore() {
		return this.routerServer;
	}



}
