package com.ggx.eventbus.group.client;

import com.ggx.core.client.GGClient;
import com.ggx.core.common.utils.GenericClassUtil;
import com.ggx.eventbus.group.client.config.EventbusClientGroupConfig;
import com.ggx.eventbus.group.client.subscriber.Subscriber;
import com.ggx.eventbus.group.client.subscriber.SubscriberInfo;

public class EventbusGroupClient{
	
	private EventbusClientGroupConfig config;
	
	private GGClient serviceClient;
	
	
	public EventbusGroupClient(EventbusClientGroupConfig config) {
		init();
	}

	public void init() {
		
	}
	
	public void start() {
		
	}
	
	/**
	 * 发布事件消息
	 *
	 * @param eventId
	 * @author zai
	 * 2020-04-11 18:12:48
	 */
	public void publishEvent(String eventId, Object data) {
		
	}

	/**
	 * 注册事件订阅
	 *
	 * @param <T>
	 * @param eventId
	 * @param subscriber
	 * @author zai
	 * 2020-04-11 22:54:45
	 */
	public <T> void subscribe(String eventId, Subscriber<T> subscriber) {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		Class<?> subscriberClass = GenericClassUtil.getGenericClass(subscriber.getClass());
		subscriberInfo.setClazz(subscriberClass);
		subscriberInfo.setSubscriber(subscriber);
		subscriberInfo.setSubscriberId(subscriberClass.getName());
		
		this.config.getSubscribeManager().subscribe(eventId, subscriberInfo);
	}

}
