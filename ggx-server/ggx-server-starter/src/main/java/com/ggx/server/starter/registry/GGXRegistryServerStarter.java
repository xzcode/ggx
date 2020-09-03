package com.ggx.server.starter.registry;

import com.ggx.core.common.config.GGXCore;
import com.ggx.registry.server.RegistryServer;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXRegistryServerStarter extends GGXBasicServerStarter{

	@Override
	public void start() {
		if (this.registryServerConfig == null) {
			this.registryServerConfig = new RegistryServerConfig();
		}
		this.registryServer = new RegistryServer(this.registryServerConfig);
		this.registryServer.start();
		
	}
	
	public RegistryServerConfig getRegistryServerConfig() {
		return registryServerConfig;
	}

	public void setRegistryServerConfig(RegistryServerConfig registryServerConfig) {
		this.registryServerConfig = registryServerConfig;
	}

	@Override
	public GGXCore getGGXCore() {
		return this.registryServer.getConfig().getServer();
	}

	

}
