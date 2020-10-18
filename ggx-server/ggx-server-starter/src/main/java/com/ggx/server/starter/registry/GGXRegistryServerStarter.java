package com.ggx.server.starter.registry;

import com.ggx.core.common.config.GGXCore;
import com.ggx.registry.server.RegistryServer;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXRegistryServerStarter extends GGXBasicServerStarter {

	public void init() {
		if (this.registryServerConfig == null) {
			this.registryServerConfig = new RegistryServerConfig();
		}
		this.registryServer = new RegistryServer(this.registryServerConfig);
	}

	@Override
	public void start() {
		this.registryServer.start();
	}


	@Override
	public GGXCore getGGXCore() {
		return this.registryServer.getConfig().getServer();
	}

}
