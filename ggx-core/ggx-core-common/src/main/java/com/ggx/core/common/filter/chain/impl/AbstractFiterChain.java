package com.ggx.core.common.filter.chain.impl;

import java.util.List;

import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.chain.FilterChain;
import com.ggx.core.common.future.GGXFuture;

public abstract class AbstractFiterChain <T> implements FilterChain<T> {

	private int filterIndex = -1;

	private List<Filter<T>> filters;

	public AbstractFiterChain(List<Filter<T>> filters) {
		this.filters = filters;
	}

	@Override
	public GGXFuture<?> doFilter(T data) throws Throwable {
		this.filterIndex++;
		try {
			return filters.get(filterIndex).doFilter(data, this);
			
		} catch (Throwable e) {
			throw e;
		}
	}

	@Override
	public void setFilters(List<Filter<T>> filters) {
		this.filters = filters;
	}

}
