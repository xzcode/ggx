package com.ggx.eventbus.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.eventbus.client.config.EventbusClientConfig;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private EventbusClientConfig config;
	
	public ConnCloseEventListener(EventbusClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
	}


	
}
