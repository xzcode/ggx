package com.ggx.eventbus.group.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.eventbus.group.client.config.EventbusClientGroupConfig;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private EventbusClientGroupConfig config;
	
	public ConnCloseEventListener(EventbusClientGroupConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
	}


	
}
