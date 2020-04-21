package com.xzcode.ggcloud.discovery.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.xzcode.ggcloud.discovery.server.config.DiscoveryServerConfig;

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
