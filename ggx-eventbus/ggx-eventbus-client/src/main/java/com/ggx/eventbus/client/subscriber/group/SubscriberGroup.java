package com.ggx.eventbus.client.subscriber.group;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.eventbus.client.subscriber.model.SubscriptionData;

/***
 * 订阅信息
 *
 * @author zai
 * 2020-04-07 11:26:42
 */
public class SubscriberGroup {
	
	//事件id
	private String eventId;
	
	//数据类型
	private Class<?> dataType;
	
	//订阅的会话集合
	private List<Subscriber> subscribers = new CopyOnWriteArrayList<>();
	
	
	public SubscriberGroup(String eventId) {
		this.eventId = eventId;
	}
	
	public void trigger(SubscriptionData subscriptionData) throws Throwable {
		for (Subscriber subscriber : subscribers) {
			subscriber.trigger(subscriptionData);
		}
	}

	public void add(Subscriber subscriber) {
		this.subscribers.add(subscriber);
	}
	
	public void remove(Subscriber subscriber) {
		this.subscribers.remove(subscriber);
	}
	
	public String getEventId() {
		return eventId;
	}
	
	public Class<?> getDataType() {
		return dataType;
	}
	
	public void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}
	
}
