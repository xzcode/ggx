package com.ggx.eventbus.client.subscriber.eventid;

public interface EventIdGenerator {
	
	String generate(Class<?> clazz);
}
