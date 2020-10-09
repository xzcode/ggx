package com.ggx.server.starter.loadbalancer;

import com.ggx.core.common.config.GGXCore;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.eventbus.server.config.EventbusServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.impl.RegistrySingleServicePorvider;
import com.ggx.router.server.RouterServer;
import com.ggx.router.server.config.RouterServerConfig;
import com.ggx.rpc.client.RpcClient;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXLoadbalancerServerStarter extends GGXBasicServerStarter {


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
		this.routerServer = new RouterServer(routerServerConfig);

		if (this.routerClientConfig == null) {
			this.routerClientConfig = new RouterClientConfig(routerServer.getSessionServiceServer());
		}
		if (this.routerClientConfig.getServiceProvider() == null) {
			this.routerClientConfig.setServiceProvider(new RegistrySingleServicePorvider(this.routerClientConfig));
		}
		this.routerClientConfig.setHostServer(this.routerServer.getSessionServiceServer());
		this.routerClient = new RouterClient(routerClientConfig);
		
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


}
