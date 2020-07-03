package com.ggx.admin.collector.client.events;

import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.spring.support.annotation.GGXEventHandler;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private GGXAdminCollectorClientConfig config;
	
	public ConnCloseEventListener(GGXAdminCollectorClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
		//断开连接，触发重连
		config.getCollectorClient().connect();
	}


	
}
