package com.ggx.rpc.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.rpc.server.config.RpcServerConfig;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private RpcServerConfig config;

	public ConnCloseEventListener(RpcServerConfig config) {
		super();
		this.config = config;
	}

	public void setConfig(RpcServerConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		
		
	}

	
}
