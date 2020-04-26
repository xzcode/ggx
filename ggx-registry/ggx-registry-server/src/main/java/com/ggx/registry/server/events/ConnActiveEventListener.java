package com.ggx.registry.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.registry.server.config.RegistryServerConfig;

public class ConnActiveEventListener implements EventListener<Void>{
	
	private RegistryServerConfig config;


	public ConnActiveEventListener(RegistryServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		
	}

}
