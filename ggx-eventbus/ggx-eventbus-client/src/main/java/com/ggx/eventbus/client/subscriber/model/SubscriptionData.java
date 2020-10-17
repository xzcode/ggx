package com.ggx.eventbus.client.subscriber.model;

/**
 * 订阅数据
 * 
 * @author zai
 * 2020-9-5 22:08:34
 */
public class SubscriptionData {
	
	private String eventId;
	
	private Object data;
	
	public SubscriptionData(String eventId, Object data) {
		this.eventId = eventId;
		this.data = data;
	}


	public String getEventId() {
		return eventId;
	}
	

	public Object getData() {
		return data;
	}
}
