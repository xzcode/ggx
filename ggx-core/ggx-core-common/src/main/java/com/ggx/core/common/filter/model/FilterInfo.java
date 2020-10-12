package com.ggx.core.common.filter.model;

import com.ggx.core.common.filter.Filter;

/**
 * 过滤器信息
 *
 * @param <T>
 * @author zai
 * 2020-06-19 17:26:08
 */
public class FilterInfo<T> {
	
	/**
	 * 过滤器
	 */
	private Filter<T> filter;
	
	/**
	 * 序号
	 */
	private int order = 1;
	
	

	public FilterInfo(Filter<T> filter) {
		super();
		this.filter = filter;
	}

	public FilterInfo(Filter<T> filter, int order) {
		super();
		this.filter = filter;
		this.order = order;
	}

	public Filter<T> getFilter() {
		return filter;
	}

	public void setFilter(Filter<T> filter) {
		this.filter = filter;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	
	

}
