package com.ggx.eventbus.client.subscriber.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.common.message.EventbusMessage;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.eventbus.client.annotation.GGXSubscriber;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.eventbus.client.subscriber.SubscriberManager;
import com.ggx.eventbus.client.subscriber.eventid.EventIdGenerator;
import com.ggx.eventbus.client.subscriber.eventid.impl.DefaultEventIdGenerator;
import com.ggx.eventbus.client.subscriber.group.SubscriberGroup;
import com.ggx.eventbus.client.subscriber.model.ControllerSubscriberMethodInfo;
import com.ggx.eventbus.client.subscriber.model.SubscriptionData;
import com.ggx.util.reflect.GGXReflectUtil;

/**
 * 订阅器管理器
 *
 * @author zai
 * 2020-04-11 18:10:43
 */
public class DefaultSubscriberManager implements SubscriberManager {
	
	
	//事件订阅器组集合
	private Map<String, SubscriberGroup> groups = new ConcurrentHashMap<String, SubscriberGroup>();
	
	private EventIdGenerator eventIdGenerator = new DefaultEventIdGenerator();
	
	private Map<Class<?>, String> eventIdCache = new ConcurrentHashMap<>();
	
	/**
	 * 获取事件id集合
	 *
	 * @return
	 * @author zai
	 * 2020-04-12 19:41:01
	 */
	@Override
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
			group.setDataType(subscriber.getDataType());
			SubscriberGroup putIfAbsent = groups.putIfAbsent(eventId, group);
			if (putIfAbsent != null) {
				group = putIfAbsent;
			}
		}
		group.add(subscriber);
		eventIdCache.put(subscriber.getDataType(), eventId);
	}
	

	@Override
	public void registerSubscriberController(Object controller) {
		
		Class<?> controllerClass = controller.getClass();
		List<Method> methods = GGXReflectUtil.getAllDeclaredMethods(controllerClass);
		for (Method method : methods) {
			method.setAccessible(true);
			ControllerSubscriberMethodInfo methodInfo = new ControllerSubscriberMethodInfo();
			GGXSubscriber annotation = method.getAnnotation(GGXSubscriber.class);
			if (annotation == null) {
				continue;
			}
			Class<?> eventDataClass = methodInfo.getDataType();
			String eventId = annotation.value();
			
			Class<?>[] parameterTypes = method.getParameterTypes();
			int i = 0;
			for (Class<?> paramType : parameterTypes) {
				if (EventbusMessage.class.isAssignableFrom(paramType)) {
					eventDataClass = paramType;
					methodInfo.setMessageParamIndex(i);
				}
				i++;
			}
			if ((eventId == null || eventId.isEmpty()) && eventDataClass  != null) {
				eventId = eventIdGenerator.generate(eventDataClass);
			}
			
			methodInfo.setDataType(eventDataClass);
			methodInfo.setMethod(method);
			methodInfo.setControllerClass(controllerClass);
			methodInfo.setControllerObj(controller);
			methodInfo.setEventId(eventId);
			methodInfo.setParamClasses(parameterTypes);
				
			subscribe(eventId, new ControllerMethodSubscriber(methodInfo));
			
			
		}
	}
	
	@Override
	public String getEventId(Class<? extends EventbusMessage> clazz) {
		return this.eventIdCache.get(clazz);
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
	@Override
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
	 * @throws Throwable 
	 */
	public void trigger(String eventId, byte[] data, Serializer serializer) throws Throwable {
		SubscriberGroup group = groups.get(eventId);
		if (group != null) {
			group.trigger(new SubscriptionData(eventId, data));
		}
	}
	
}
