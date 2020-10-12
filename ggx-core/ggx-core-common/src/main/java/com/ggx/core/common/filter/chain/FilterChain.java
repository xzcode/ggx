package com.ggx.core.common.filter.chain;

import com.ggx.core.common.filter.Filter;

public interface FilterChain<T> {
	
	/**
	 * 消息过滤
	 *
	 * @param data
	 * @param filterChain
	 * @author zai
	 * 2020-10-12 09:49:03
	 */
	void doFilter(T data);
	
	/**
	 * 添加过滤器
	 *
	 * @param filter
	 * @author zai
	 * 2020-10-12 11:15:03
	 */
	void addFilter(Filter<T> filter);
	
	/**
	 * 移除过滤器
	 *
	 * @param filter
	 * @author zai
	 * 2020-10-12 11:15:10
	 */
	void removeFilter(Filter<T> filter);
	

}
