package com.ggx.core.common.filter.chain;

/**
 * 会话消息过滤器统一接口
 * 
 * @param <T>
 * @author zzz
 * 2019-10-08 18:20:21
 */
public interface ChainFilter<T> {
	
	/**
	 * 消息接收过滤
	 *
	 * @param data
	 * @param filterChain
	 * @author zai
	 * 2020-10-12 09:49:03
	 */
	void doFilter(T data, FilterChain<T> filterChain);

}