package com.ggx.monitor.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.monitor.client.config.GameMonitorClientConfig;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private GameMonitorClientConfig config;
	
	public ConnCloseEventListener(GameMonitorClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
		//断开连接，触发重连
		config.getGameMonitorClient().connect();
	}


	
}
