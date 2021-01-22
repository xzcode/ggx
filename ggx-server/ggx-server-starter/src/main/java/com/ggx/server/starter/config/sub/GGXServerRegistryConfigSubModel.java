package com.ggx.server.starter.config.sub;

import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.server.config.RegistryServerConfig;

public class GGXServerRegistryConfigSubModel {
	
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
