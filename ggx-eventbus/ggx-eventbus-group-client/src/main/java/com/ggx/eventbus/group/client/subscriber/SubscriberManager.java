package com.ggx.eventbus.group.client.subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.eventbus.client.subscriber.SubscriberGroup;
import com.ggx.eventbus.client.subscriber.SubscriberInfo;

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
	public <T> void subscribe(String eventId, SubscriberInfo subscriberInfo) {
		SubscriberGroup group = groups.get(eventId);
		if (group == null) {
			group = new SubscriberGroup(eventId);
			SubscriberGroup putIfAbsent = groups.putIfAbsent(eventId, group);
			if (putIfAbsent != null) {
				group = putIfAbsent;
			}
		}
		group.add(subscriberInfo);
	}
	
	/**
	 * 移除订阅器
	 *
	 * @param eventId
	 * @param subscriber
	 * @author zai
	 * 2020-04-11 18:10:05
	 */
	public void removeSubscriber(String eventId, SubscriberInfo subscriber) {
		SubscriberGroup group = groups.get(eventId);
		if (group != null) {
			group.remove(subscriber);
		}
	}
	
	/**
	 * 获取订阅器相关信息
	 *
	 * @param eventId
	 * @param subscriberId
	 * @return
	 * @author zai
	 * 2020-04-12 01:41:29
	 */
	public SubscriberInfo getSubscriberInfo(String eventId, String subscriberId) {
		SubscriberGroup group = this.groups.get(eventId);
		if (group != null) {
			return group.getSubscriberInfo(subscriberId);
		}
		return null;
	}
	
	/**
	 * 触发订阅器
	 *
	 * @param eventId
	 * @param eventbusMessage
	 * @author zai
	 * 2020-04-11 18:09:54
	 */
	public void trigger(String eventId, String subscriberId, Object data) {
		SubscriberGroup group = groups.get(eventId);
		if (group != null) {
			group.trigger(subscriberId, data);
		}
	}
	
}
