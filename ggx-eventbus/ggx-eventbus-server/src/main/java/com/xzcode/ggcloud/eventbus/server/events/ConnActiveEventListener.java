package com.xzcode.ggcloud.eventbus.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.xzcode.ggcloud.eventbus.server.config.EventbusServerConfig;

public class ConnActiveEventListener implements EventListener<Void>{
	
	private EventbusServerConfig config;


	public ConnActiveEventListener(EventbusServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		
	}

}
