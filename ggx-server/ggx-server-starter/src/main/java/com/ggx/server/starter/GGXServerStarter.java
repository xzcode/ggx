package com.ggx.server.starter;

import com.ggx.core.common.config.GGXCoreSupport;
import com.ggx.eventbus.client.subscriber.Subscriber;

public interface GGXServerStarter extends GGXCoreSupport{
	
	
	/**
	 * 启动
	 *
	 * @author zai
	 * 2020-08-21 18:24:48
	 */
	void start();
	
	
	/**
	 * 关闭
	 *
	 * @author zai
	 * 2020-08-24 14:29:21
	 */
	void shutdown();
	
	
	/**
	 * 订阅eventbus事件
	 * @param eventId
	 * @param subscriber
	 */
	void subscribe(String eventId, Subscriber<?> subscriber);
	
	/**
	 * 发布eventbus事件
	 * @param eventId
	 * @param subscriber
	 */
	void publish(String eventId, Object data);

}
