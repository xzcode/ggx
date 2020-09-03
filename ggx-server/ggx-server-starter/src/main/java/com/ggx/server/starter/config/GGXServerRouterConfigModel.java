package com.ggx.server.starter.config;

import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.server.config.RouterServerConfig;

public class GGXServerRouterConfigModel {
	
	protected RouterServerConfig server;
	
	protected RouterClientConfig client;

	public RouterServerConfig getServer() {
		return server;
	}

	public void setServer(RouterServerConfig server) {
		this.server = server;
	}

	public RouterClientConfig getClient() {
		return client;
	}

	public void setClient(RouterClientConfig client) {
		this.client = client;
	}

	

}
