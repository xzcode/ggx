package com.ggx.rpc.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.rpc.client.config.RpcClientConfig;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private RpcClientConfig config;
	
	public ConnCloseEventListener(RpcClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
	}


	
}
