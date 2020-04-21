package com.xzcode.ggcloud.eventbus.client.subscriber;

/**
 * 消息订阅器接口
 *
 * @author zai
 * 2020-04-11 18:03:17
 */
public interface Subscriber<T> {

	/**
	 * 触发订阅调用
	 *
	 * @param data
	 * @author zai
	 * 2020-04-11 17:35:54
	 */
	void trigger(T data);
	
}
