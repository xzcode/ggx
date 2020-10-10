package com.ggx.core.common.filter;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.model.Message;

/**
 * 会话消息过滤器统一接口
 * 
 * @param <T>
 * @author zzz
 * 2019-10-08 18:20:21
 */
public interface Filter<T extends Message> {
	
	void doFilter(MessageData<T> data, FilterChain filterChain);

}
