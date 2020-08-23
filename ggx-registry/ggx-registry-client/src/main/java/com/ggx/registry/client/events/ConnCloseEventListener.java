package com.ggx.registry.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.common.service.ServiceManager;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private RegistryClientConfig config;
	
	public ConnCloseEventListener(RegistryClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
		config.getRegistryManager().getRegistriedInfo().setActive(false);
		ServiceManager serviceManager = config.getServiceManager();
		serviceManager.clearAllServices();
		
		GGLoggerUtil.getLogger(this).warn("Disconnected from registry server [{}]!", eventData.getSession().getChannel());
		
		//断开连接，触发重连
		config.getRegistryClient().connect();
		
		
	}


	
}
