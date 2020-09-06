package com.ggx.eventbus.client.subscriber;

import com.ggx.core.common.handler.serializer.Serializer;

/**
 * 订阅数据
 * 
 * @author zai
 * 2020-9-5 22:08:34
 */
public class SubscriptionData {
	
	private String eventId;
	
	private byte[] data;
	
	private Object cacheDataObject;
	
	private Serializer serializer;
	
	
	
	
	public SubscriptionData(String eventId, byte[] data, Serializer serializer) {
		super();
		this.eventId = eventId;
		this.data = data;
		this.serializer = serializer;
	}


	public String getEventId() {
		return eventId;
	}
	

	@SuppressWarnings("unchecked")
	public <T> T getData(Class<T> clazz) {
		if (this.cacheDataObject != null) {
			return (T) this.cacheDataObject;
		}
		try {
			return this.serializer.deserialize(data, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
