package com.xzcode.ggcloud.eventbus.client.subscriber;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 订阅信息
 *
 * @author zai
 * 2020-04-07 11:26:42
 */
public class SubscriberGroup {
	
	//事件id
	private String eventId;
	
	//订阅的会话集合
	private Map<String, SubscriberInfo> subscriberInfos = new ConcurrentHashMap<>();
	
	
	public SubscriberGroup(String eventId) {
		super();
		this.eventId = eventId;
	}
	
	@SuppressWarnings("unchecked")
	public void trigger(String subscriberId, Object data) {
		for (Entry<String, SubscriberInfo> entry : subscriberInfos.entrySet()) {
			Subscriber<Object> subscriber = (Subscriber<Object>) entry.getValue().getSubscriber();
			subscriber.trigger(data);
		}
	}

	public void add(SubscriberInfo subscriberInfo) {
		this.subscriberInfos.put(subscriberInfo.getSubscriberId(), subscriberInfo);
	}
	
	public void remove(SubscriberInfo subscriberInfo) {
		this.subscriberInfos.remove(subscriberInfo.getSubscriberId());
	}
	
	public SubscriberInfo getSubscriberInfo(String subscriberId) {
		return this.subscriberInfos.get(subscriberId);
	}
	
	public void remove(String subscriberId) {
		this.subscriberInfos.remove(subscriberId);
	}
	
	public String getEventId() {
		return eventId;
	}
	
}
