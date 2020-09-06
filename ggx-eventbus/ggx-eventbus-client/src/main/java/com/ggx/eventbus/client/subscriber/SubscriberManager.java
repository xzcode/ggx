package com.ggx.eventbus.client.subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.handler.serializer.Serializer;

/**
 * 订阅器管理器
 *
 * @author zai
 * 2020-04-11 18:10:43
 */
public class SubscriberManager {
	
	//事件订阅器组集合
	private Map<String, SubscriberGroup> groups = new ConcurrentHashMap<String, SubscriberGroup>();
	
	/**
	 * 获取事件id集合
	 *
	 * @return
	 * @author zai
	 * 2020-04-12 19:41:01
	 */
	public List<String> getEventIdList() {
		return new ArrayList<String>(groups.keySet());
	}
	
	/**
	 * 添加订阅器
	 *
	 * @param eventId
	 * @param subscriber
	 * @author zai
	 * 2020-04-11 18:10:20
	 */
	public <T> void subscribe(String eventId, Subscriber subscriber) {
		SubscriberGroup group = groups.get(eventId);
		if (group == null) {
			group = new SubscriberGroup(eventId);
			SubscriberGroup putIfAbsent = groups.putIfAbsent(eventId, group);
			if (putIfAbsent != null) {
				group = putIfAbsent;
			}
		}
		group.add(subscriber);
	}
	
	/**
	 * 移除订阅器
	 *
	 * @param eventId
	 * @param subscriber
	 * @author zai
	 * 2020-04-11 18:10:05
	 */
	public void removeSubscriber(String eventId, Subscriber subscriber) {
		SubscriberGroup group = groups.get(eventId);
		if (group != null) {
			group.remove(subscriber);
		}
	}
	
	
	/**
	 * 
	 * 获取订阅组
	 *
	 * @param eventId
	 * @return
	 * @author zai
	 * 2020-05-18 18:33:14
	 */
	public SubscriberGroup getSubscriberGroup(String eventId) {
		return this.groups.get(eventId);
	}
	
	/**
	 * 触发订阅器
	 *
	 * @param eventId
	 * @param eventbusMessage
	 * @author zai
	 * 2020-04-11 18:09:54
	 */
	public void trigger(String eventId, byte[] data, Serializer serializer) {
		SubscriberGroup group = groups.get(eventId);
		if (group != null) {
			group.trigger(new SubscriptionData(eventId, data, serializer));
		}
	}
	
}
