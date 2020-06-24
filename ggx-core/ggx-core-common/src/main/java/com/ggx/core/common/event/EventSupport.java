package com.ggx.core.common.event;

import com.ggx.core.common.event.model.EventData;

/**
 * 事件支持
 * 
 * @author zai 2019-12-05 10:50:19
 */
public interface EventSupport extends EventManager {

	EventManager getEventManager();

	@Override
	default <T> void addEventListener(String event, EventListener<T> listener) {
		getEventManager().addEventListener(event, listener);
	}

	@Override
	default <T> void removeEventListener(String event, EventListener<T> listener) {
		getEventManager().removeEventListener(event, listener);
	}

	@Override
	default <T> boolean hasEventListener(String event, EventListener<T> listener) {
		return getEventManager().hasEventListener(event, listener);
	}

	@Override
	default void clearEventListener(String event) {
		getEventManager().clearEventListener(event);
	}

	default <T> void emitEvent(EventData<T> eventData) {
		getEventManager().emitEvent(eventData);
	}

	@Override
	default <T> boolean hasEventListener(String event) {
		return getEventManager().hasEventListener(event);
	}

	@Override
	default boolean isEmpty() {
		return getEventManager().isEmpty();
	}

	@Override
	default <T> EventListenerGroup<T> createEventListenerGroup() {
		return getEventManager().createEventListenerGroup();
	}

}
