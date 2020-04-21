package com.xzcode.ggcloud.discovery.server.events;

import com.xzcode.ggcloud.discovery.server.config.DiscoveryServerConfig;
import com.xzcode.ggserver.core.common.event.EventListener;
import com.xzcode.ggserver.core.common.event.model.EventData;

public class ConnActiveEventListener implements EventListener<Void>{
	
	private DiscoveryServerConfig config;


	public ConnActiveEventListener(DiscoveryServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		
	}

}
