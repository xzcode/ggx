package com.ggx.admin.collector.server.events;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;

public class ConnActiveEventListener implements EventListener<Void>{
	
	@Autowired
	private GGXAdminCollectorServerConfig config;

	

	public ConnActiveEventListener(GGXAdminCollectorServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		
	}

}
