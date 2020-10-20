package com.ggx.group.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.group.server.config.SessionGroupServerConfig;

public class ConnActiveEventListener implements EventListener<Void>{
	
	private SessionGroupServerConfig config;


	public ConnActiveEventListener(SessionGroupServerConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		
	}

}
