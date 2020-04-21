package com.xzcode.ggcloud.eventbus.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.xzcode.ggcloud.eventbus.client.config.EventbusClientConfig;

public class ConnOpenEventListener implements EventListener<Void>{

	private EventbusClientConfig config;
	
	public ConnOpenEventListener(EventbusClientConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> e) {
	}

}
