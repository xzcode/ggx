package com.ggx.eventbus.group.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.utils.GenericClassUtil;
import com.ggx.eventbus.client.EventbusClient;
import com.ggx.eventbus.client.config.EventbusClientConfig;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.eventbus.client.subscriber.SubscriberInfo;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;

import io.netty.channel.nio.NioEventLoopGroup;

public class EventbusGroupClient{
	
	private EventbusGroupClientConfig config;
	
	protected final Map<String, EventbusClient> eventbusClients = new ConcurrentHashMap<String, EventbusClient>();
	protected final List<EventbusClient> eventbusClientList = new CopyOnWriteArrayList<EventbusClient>();
	
	
	public EventbusGroupClient(EventbusGroupClientConfig config) {
		this.config = config;
		init();
	}

	public void init() {
		
		if (this.config.getSharedEventLoopGroup() == null) {
			this.config.setSharedEventLoopGroup(new NioEventLoopGroup(this.config.getWorkThreadSize(), new GGThreadFactory("gg-evt-group-", false)));
		}
		
		RegistryClient registryClient = this.config.getRegistryClient();
		ServiceManager serviceManager = registryClient.getConfig().getServiceManager();
		
		//
		serviceManager.addRegisterListener(service -> {
			addEventbusServerService(service);
		});
		
		//解除注册
		serviceManager.addUnregisterListener(service -> {
			removeEventbusServerService(service);
		});
		
		serviceManager.addUpdateListener(service -> {
			addEventbusServerService(service);
		});
		
	}
	
	public void addEventbusServerService(ServiceInfo serviceInfo) {
		
		Map<String, String> customData = serviceInfo.getCustomData();
		String eventbugGroupId = customData.get(EventbusConstant.REGISTRY_CUSTOM_EVENTBUS_GROUP_KEY);
		if (eventbugGroupId == null || !eventbugGroupId.equals(this.config.getEventbusGroupId())) {
			return;
		}
		
		String portString = customData.get(EventbusConstant.REGISTRY_CUSTOM_EVENTBUS_PORT_KEY);
		int port = Integer.valueOf(portString);
		EventbusClientConfig eventbusClientConfig = new EventbusClientConfig();
		eventbusClientConfig.setServerHost(serviceInfo.getHost());
		eventbusClientConfig.setServerPort(port);
		eventbusClientConfig.setSubscribeManager(this.config.getSubscribeManager());
		eventbusClientConfig.setSharedEventLoopGroup(this.config.getSharedEventLoopGroup());
		EventbusClient eventbusClient = new EventbusClient(eventbusClientConfig);
		
		EventbusClient putIfAbsent = this.eventbusClients.putIfAbsent(serviceInfo.getServiceId(), eventbusClient);
		if (putIfAbsent == null) {
			//添加到list
			this.eventbusClientList.add(eventbusClient);
			eventbusClient.start();
		}else {
			eventbusClient = putIfAbsent;
		}
		
	}
	
	public void removeEventbusServerService(ServiceInfo serviceInfo) {
		EventbusClient eventbusClient = this.eventbusClients.remove(serviceInfo.getServiceId());
		if (eventbusClient != null) {
			//从list移除
			this.eventbusClientList.remove(eventbusClient);
			eventbusClient.shutdown();
		}
	}
	
	
	/**
	 * 发布事件消息
	 *
	 * @param eventId
	 * @param data
	 * @author zai
	 * 2020-05-12 14:29:38
	 */
	public void publishEvent(String eventId, Object data) {
		if (this.eventbusClientList.size() == 0) {
			return;
		}
		EventbusClient eventbusClient = null;
		if (this.eventbusClientList.size() == 1) {
			eventbusClient = this.eventbusClientList.get(0);
		}else {
			eventbusClient = this.eventbusClientList.get(ThreadLocalRandom.current().nextInt(this.eventbusClientList.size()));
		}
		
		eventbusClient.publishEvent(eventId, data);
		
		
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
		Class<?> subscriberClass = GenericClassUtil.getInterfaceGenericClass(subscriber.getClass());
		subscriberInfo.setClazz(subscriberClass);
		subscriberInfo.setSubscriber(subscriber);
		subscriberInfo.setSubscriberId(subscriberClass.getName());
		
		this.config.getSubscribeManager().subscribe(eventId, subscriberInfo);
	}

}
