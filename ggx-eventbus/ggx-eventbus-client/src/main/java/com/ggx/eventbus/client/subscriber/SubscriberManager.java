package com.ggx.eventbus.client.subscriber;

import java.util.List;

import com.ggx.eventbus.client.subscriber.group.SubscriberGroup;
/**
 * 订阅管理器
 *
 * @author zai
 * 2020-10-17 16:22:36
 */
public interface SubscriberManager {

	/**
	 * 获取事件id集合
	 *
	 * @return
	 * @author zai
	 * 2020-04-12 19:41:01
	 */
	List<String> getEventIdList();

	/**
	 * 注册订阅控制器
	 *
	 * @param controller
	 * @author zai
	 * 2020-10-17 16:22:25
	 */
	void registerSubscriberController(Object controller);

	/**
	 * 
	 * 获取订阅组
	 *
	 * @param eventId
	 * @return
	 * @author zai
	 * 2020-05-18 18:33:14
	 */
	SubscriberGroup getSubscriberGroup(String eventId);

}