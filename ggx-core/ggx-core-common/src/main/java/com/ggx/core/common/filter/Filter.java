package com.ggx.core.common.filter;

/**
 * 消息过滤器统一接口
 * 
 * @param <T>
 * @author zai
 * 2019-10-08 18:20:21
 */
public interface Filter<T> {
	
	boolean doReceiveFilter(T receiveData);
		
	boolean doSendFilter(T sendData);

}
