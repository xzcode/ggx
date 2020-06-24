package com.ggx.monitor.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.monitor.server.config.GameMonitorServerConfig;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private GameMonitorServerConfig config;

	public ConnCloseEventListener(GameMonitorServerConfig config) {
		super();
		this.config = config;
	}

	public void setConfig(GameMonitorServerConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		//连接关闭
		
		
		
	}

	
}
