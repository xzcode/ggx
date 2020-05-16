package com.ggx.eventbus.group.client.config;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.eventbus.client.subscriber.SubscriberManager;
import com.ggx.registry.client.RegistryClient;

import io.netty.channel.EventLoopGroup;

/**
 * EventbusClient配置
 *
 * @author zai
 * 2020-04-10 11:58:47
 */
public class EventbusGroupClientConfig {

	//注册中心客户端
	protected RegistryClient registryClient;
	
	//共享线程组
	protected EventLoopGroup sharedEventLoopGroup;
	
	// 订阅者管理器
	protected SubscriberManager subscribeManager = new SubscriberManager();
	
	
	//事件组id
	protected String eventbusGroupId = EventbusConstant.DEFAULT_EVENTBUS_GROUP_ID;

	
	// 工作线程数
	protected int workThreadSize = 8;

	// 连接数
	protected int connectionSize = 8;
	
	public RegistryClient getRegistryClient() {
		return registryClient;
	}

	public void setRegistryClient(RegistryClient registryClient) {
		this.registryClient = registryClient;
	}

	public EventLoopGroup getSharedEventLoopGroup() {
		return sharedEventLoopGroup;
	}

	public void setSharedEventLoopGroup(EventLoopGroup sharedEventLoopGroup) {
		this.sharedEventLoopGroup = sharedEventLoopGroup;
	}
	
	public String getEventbusGroupId() {
		return eventbusGroupId;
	}
	
	public void setEventbusGroupId(String eventbusGroupId) {
		this.eventbusGroupId = eventbusGroupId;
	}
	
	public SubscriberManager getSubscribeManager() {
		return subscribeManager;
	}

	public int getWorkThreadSize() {
		return workThreadSize;
	}

	public void setWorkThreadSize(int workThreadSize) {
		this.workThreadSize = workThreadSize;
	}

	public int getConnectionSize() {
		return connectionSize;
	}

	public void setConnectionSize(int connectionSize) {
		this.connectionSize = connectionSize;
	}
	
	
	
}
