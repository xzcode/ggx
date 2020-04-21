package com.xzcode.ggcloud.discovery.client.events;

import com.xzcode.ggcloud.discovery.client.config.DiscoveryClientConfig;
import com.xzcode.ggcloud.discovery.common.service.ServiceManager;
import com.xzcode.ggserver.core.common.event.EventListener;
import com.xzcode.ggserver.core.common.event.model.EventData;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private DiscoveryClientConfig config;
	
	public ConnCloseEventListener(DiscoveryClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
		config.getRegistryManager().getRegistriedInfo().setActive(false);
		ServiceManager serviceManager = config.getServiceManager();
		serviceManager.clearAllServices();
		//断开连接，触发重连
		config.getDiscoveryClient().connect();
	}


	
}
