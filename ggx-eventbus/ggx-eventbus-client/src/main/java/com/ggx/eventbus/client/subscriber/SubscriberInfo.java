package com.ggx.eventbus.client.subscriber;

/**
 * 订阅者信息
 *
 * @author zai
 * 2020-04-11 18:59:00
 */
public class SubscriberInfo {
	
	/**
	 * 事件数据class
	 */
	protected Class<?> clazz;
	
	/**
	 * 订阅器对象
	 */
	protected Subscriber<?> subscriber;
	
	/**
	 * 订阅器id
	 */
	protected String subscriberId;
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Subscriber<?> getSubscriber() {
		return subscriber;
	}
	
	public void setSubscriber(Subscriber<?> subscriber) {
		this.subscriber = subscriber;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	
	

}
