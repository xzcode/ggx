package com.xzcode.ggcloud.eventbus.client.subscriber;

/**
 * eventbus事件消息
 *
 * @param <T>
 * @author zai
 * 2020-04-11 17:39:04
 */
public class EventbusMessage<T> {
	
	protected T data;
	
	
	public T getData() {
		return data;
	}
	
	
	public void setData(T data) {
		this.data = data;
	}

}
