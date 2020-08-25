package com.ggx.server.spring.boot.starter.config;

import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.server.config.RegistryServerConfig;

public class GGXServerRegistryConfigModel {
	
	protected RegistryServerConfig server;
	
	protected RegistryClientConfig client;

	public RegistryServerConfig getServer() {
		return server;
	}

	public void setServer(RegistryServerConfig server) {
		this.server = server;
	}

	public RegistryClientConfig getClient() {
		return client;
	}

	public void setClient(RegistryClientConfig client) {
		this.client = client;
	}
	
	
	
	

}
