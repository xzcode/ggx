package com.ggx.server.starter;

import com.ggx.core.common.config.GGXCore;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.server.starter.config.GGXServerConfig;
import com.ggx.server.starter.constant.GGXServerMode;
import com.ggx.server.starter.core.GGXCoreServerStarter;
import com.ggx.server.starter.eventbus.GGXEventbusClientStarter;
import com.ggx.server.starter.eventbus.GGXEventbusServerStarter;
import com.ggx.server.starter.gateway.GGXGatewayStarter;
import com.ggx.server.starter.loadbalancer.GGXLoadbalancerServerStarter;
import com.ggx.server.starter.registry.GGXRegistryServerStarter;

public class GGXServer implements GGXServerStarter{
	
	private GGXServerStarter serverStarter;
	
	private String mode = GGXServerMode.CORE_SERVER;
	
	private GGXServerConfig config;

	public GGXServer(GGXServerConfig config) {
		this.config = config;
		this.mode = this.config.getMode();
		init();
	}

	public GGXServer() {
		init();
	}
	
	private void init() {
		switch (mode) {
		case GGXServerMode.CORE_SERVER:
			GGXCoreServerStarter ggxCoreServerStarter = new GGXCoreServerStarter();
			ggxCoreServerStarter.setCoreServerConfig(config.getCore());
			if (config.getEventbus().getClient() != null) {
				ggxCoreServerStarter.setEventbusGroupClientConfig(ggxCoreServerStarter.getEventbusGroupClientConfig());
			}
			ggxCoreServerStarter.setRegistryClientConfig(ggxCoreServerStarter.getRegistryClientConfig());
			ggxCoreServerStarter.init();
			this.serverStarter = ggxCoreServerStarter;
			break;
		case GGXServerMode.LOADBALANCER:
			GGXLoadbalancerServerStarter ggxLoadbalancerServerStarter = new GGXLoadbalancerServerStarter();
			ggxLoadbalancerServerStarter.setRouterServerConfig(config.getRouter().getServer());
			ggxLoadbalancerServerStarter.setRouterClientConfig(config.getRouter().getClient());
			if (config.getEventbus() != null && config.getEventbus().getClient() != null) {
				ggxLoadbalancerServerStarter.setEventbusGroupClientConfig(config.getEventbus().getClient());
			}
			ggxLoadbalancerServerStarter.setRegistryClientConfig(config.getRegistry().getClient());
			ggxLoadbalancerServerStarter.init();
			this.serverStarter = ggxLoadbalancerServerStarter;
			break;
		case GGXServerMode.REGISTRY_SERVER:
			GGXRegistryServerStarter ggxRegistryServerStarter = new GGXRegistryServerStarter();
			ggxRegistryServerStarter.setRegistryServerConfig(config.getRegistry().getServer());
			ggxRegistryServerStarter.init();
			this.serverStarter = ggxRegistryServerStarter;
			break;
		case GGXServerMode.GATEWAY:
			GGXGatewayStarter ggxGatewayStarter = new GGXGatewayStarter();
			ggxGatewayStarter.setCoreServerConfig(config.getCore());
			ggxGatewayStarter.setRegistryClientConfig(config.getRegistry().getClient());
			if (config.getEventbus() != null && config.getEventbus().getClient() != null) {
				ggxGatewayStarter.setEventbusGroupClientConfig(config.getEventbus().getClient());
			}
			ggxGatewayStarter.setRouterClientConfig(config.getRouter().getClient());
			ggxGatewayStarter.init();
			this.serverStarter = ggxGatewayStarter;
			break;
		case GGXServerMode.EVENTBUS_CLIENT:
			GGXEventbusClientStarter ggxEventbusClientStarter = new GGXEventbusClientStarter();
				ggxEventbusClientStarter.setRegistryClientConfig(config.getRegistry().getClient());
			if (config.getEventbus() != null && config.getEventbus().getClient() != null) {
					ggxEventbusClientStarter.setEventbusGroupClientConfig(config.getEventbus().getClient());
			}
			ggxEventbusClientStarter.init();
			this.serverStarter = ggxEventbusClientStarter;
			break;
		case GGXServerMode.EVENTBUS_SERVER:
			GGXEventbusServerStarter ggxEventbusServerStarter = new GGXEventbusServerStarter();
			ggxEventbusServerStarter.setRegistryClientConfig(config.getRegistry().getClient());
			if (config.getEventbus() != null && config.getEventbus().getServer() != null) {
					ggxEventbusServerStarter.setEventbusServerConfig(config.getEventbus().getServer());
			}
			ggxEventbusServerStarter.init();
			this.serverStarter = ggxEventbusServerStarter;
			break;
		default:
			break;
		}
	}
	
	public void subscribe(String eventId, Subscriber subscriber) {
		this.serverStarter.subscribe(eventId, subscriber);
	}
	

	@Override
	public void publish(String eventId, Object data) {
		this.serverStarter.publish(eventId, data);
	}

	@Override
	public void start() {
		serverStarter.start();
	}

	@Override
	public void shutdown() {
		serverStarter.shutdown();
	}

	@Override
	public GGXCore getGGXCore() {
		return this.serverStarter;
	}

	
	
	

}
