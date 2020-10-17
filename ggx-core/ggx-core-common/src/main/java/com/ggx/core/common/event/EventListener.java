package com.ggx.core.common.event;

import com.ggx.core.common.event.model.EventData;

public interface EventListener<T> {
	
	public default String getEventId() {
		return null;
	}
	
	
	
	void onEvent(EventData<T> eventData) throws Throwable;
	
}
