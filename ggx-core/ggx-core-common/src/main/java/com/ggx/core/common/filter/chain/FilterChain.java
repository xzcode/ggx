package com.ggx.core.common.filter.chain;

import java.util.List;

import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.future.GGXFuture;

public interface FilterChain<T> {
	
	/**
	 * 消息过滤
	 *
	 * @param data
	 * @param filterChain
	 * @author zai
	 * 2020-10-12 09:49:03
	 */
	GGXFuture<?> doFilter(T data) throws Throwable;
	
	/**
	 * 设置过滤器集合
	 *
	 * @param filters
	 * @author zai
	 * 2020-10-12 16:53:35
	 */
	void setFilters(List<Filter<T>> filters);
	
	
	
	

}
