package com.ggx.registry.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.utils.logger.GGLoggerUtil;

public class ConnHeartbeatLostEventListener implements EventListener<Void>{
	

	@Override
	public void onEvent(EventData<Void> eventData) {
		
		GGLoggerUtil.getLogger(this).warn("Service heart beat lost! ");
		
	}

	
}
