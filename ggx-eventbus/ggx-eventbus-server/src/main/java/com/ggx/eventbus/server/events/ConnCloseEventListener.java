package com.ggx.eventbus.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.eventbus.server.config.EventbusServerConfig;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private EventbusServerConfig config;

	public ConnCloseEventListener(EventbusServerConfig config) {
		super();
		this.config = config;
	}

	public void setConfig(EventbusServerConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		
		
	}

	
}
