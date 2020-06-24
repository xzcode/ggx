package com.ggx.monitor.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.monitor.server.config.GameMonitorServerConfig;

public class ConnActiveEventListener implements EventListener<Void>{
	
	private GameMonitorServerConfig config;


	public ConnActiveEventListener(GameMonitorServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		
	}

}
