package com.ggx.server.starter.config.sub;

import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.eventbus.server.config.EventbusServerConfig;

public class GGXServerEventbusConfigSubModel {
	
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
