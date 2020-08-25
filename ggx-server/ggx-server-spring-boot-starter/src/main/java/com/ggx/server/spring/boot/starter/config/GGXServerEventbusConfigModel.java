package com.ggx.server.spring.boot.starter.config;

import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.eventbus.server.config.EventbusServerConfig;

public class GGXServerEventbusConfigModel {
	
	protected EventbusServerConfig server;
	
	protected EventbusGroupClientConfig client;

	public EventbusServerConfig getServer() {
		return server;
	}

	public void setServer(EventbusServerConfig server) {
		this.server = server;
	}

	public EventbusGroupClientConfig getClient() {
		return client;
	}

	public void setClient(EventbusGroupClientConfig client) {
		this.client = client;
	}
	
	

}
