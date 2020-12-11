package com.ggx.rpc.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.rpc.server.config.RpcServerConfig;

public class ConnActiveEventListener implements EventListener<Void>{
	
	protected RpcServerConfig config;


	public ConnActiveEventListener(RpcServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		
	}

}
