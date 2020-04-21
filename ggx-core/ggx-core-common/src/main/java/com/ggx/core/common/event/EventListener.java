package com.ggx.core.common.event;

import com.ggx.core.common.event.model.EventData;

public interface EventListener<T> {
	
	void onEvent(EventData<T> eventData);
	
}
