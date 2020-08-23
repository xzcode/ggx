package com.ggx.admin.collector.server.events;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.spring.support.annotation.GGXEventHandler;

@GGXEventHandler(GGXCoreEvents.Connection.CLOSED)
public class ConnCloseEventListener implements EventListener<Void>{
	
	@Autowired
	private GGXAdminCollectorServerConfig config;


	public ConnCloseEventListener(GGXAdminCollectorServerConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
		//连接关闭
		
	}

	
}
