package com.xzcode.ggcloud.eventbus.client.events;

import com.xzcode.ggcloud.eventbus.client.config.EventbusClientConfig;
import com.xzcode.ggserver.core.common.event.EventListener;
import com.xzcode.ggserver.core.common.event.model.EventData;

public class ConnOpenEventListener implements EventListener<Void>{

	private EventbusClientConfig config;
	
	public ConnOpenEventListener(EventbusClientConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> e) {
	}

}
