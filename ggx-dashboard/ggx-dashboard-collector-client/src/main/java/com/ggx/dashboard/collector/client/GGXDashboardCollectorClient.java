package com.ggx.dashboard.collector.client;

import com.ggx.dashboard.collector.client.config.GGXDashboardCollectorClientConfig;
import com.ggx.eventbus.client.EventbusClient;
import com.ggx.registry.client.RegistryClient;

public class GGXDashboardCollectorClient{

	private GGXDashboardCollectorClientConfig config;


	public GGXDashboardCollectorClient(GGXDashboardCollectorClientConfig config) {
		this.config = config;
		init();
	}
	
	private void init() {
		RegistryClient registryClient = this.config.getRegistryClient();
		EventbusClient eventbusClient = this.config.getEventbusClient();
		
	}
	

}
