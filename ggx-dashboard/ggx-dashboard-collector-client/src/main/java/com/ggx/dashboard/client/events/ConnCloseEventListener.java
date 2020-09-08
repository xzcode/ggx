package com.ggx.dashboard.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.dashboard.client.config.GGXDashboardClientConfig;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private GGXDashboardClientConfig config;
	
	public ConnCloseEventListener(GGXDashboardClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
		//断开连接，触发重连
		config.getCollectorClient().connect();
	}


	
}
