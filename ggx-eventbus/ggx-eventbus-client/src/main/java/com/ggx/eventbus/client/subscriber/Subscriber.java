package com.ggx.eventbus.client.subscriber;

import com.ggx.eventbus.client.subscriber.model.SubscriptionData;

/**
 * 消息订阅器接口
 *
 * @author zai
 * 2020-04-11 18:03:17
 */
public interface Subscriber {
	
	
	Class<?> getDataType();

	/**
	 * 触发订阅调用
	 *
	 * @param data
	 * @author zai
	 * 2020-04-11 17:35:54
	 * @throws Throwable 
	 */
	void trigger(SubscriptionData subData) throws Throwable;
	
}
