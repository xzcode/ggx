package com.xzcode.ggcloud.eventbus.server.events;

import com.xzcode.ggcloud.eventbus.server.config.EventbusServerConfig;
import com.xzcode.ggserver.core.common.event.EventListener;
import com.xzcode.ggserver.core.common.event.model.EventData;

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
