package com.ggx.server.starter;

import com.ggx.server.starter.config.GGXServerConfigModel;
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

	public GGXServer(GGXServerConfigModel configModel) {
		this.mode = configModel.getMode();
		init();
	}

	public GGXServer() {
		init();
	}
	
	private void init() {
		switch (mode) {
		case GGXServerMode.CORE_SERVER:
			serverStarter = new GGXCoreServerStarter();
			break;
		case GGXServerMode.LOADBALANCER:
			serverStarter = new GGXLoadbalancerServerStarter();
			break;
		case GGXServerMode.REGISTRY_SERVER:
			serverStarter = new GGXRegistryServerStarter();
			break;
		case GGXServerMode.GATEWAY:
			serverStarter = new GGXGatewayStarter();
			break;
		case GGXServerMode.EVENTBUS_CLIENT:
			serverStarter = new GGXEventbusClientStarter();
			break;
		case GGXServerMode.EVENTBUS_SERVER:
			serverStarter = new GGXEventbusServerStarter();
			break;
		default:
			break;
		}
	}

	@Override
	public void start() {
		serverStarter.start();
	}

	@Override
	public void shutdown() {
		serverStarter.shutdown();
	}
	
	
	

}
