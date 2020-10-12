package com.ggx.core.common.filter.chain.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.core.common.filter.chain.ChainFilter;
import com.ggx.core.common.filter.chain.FilterChain;

public abstract class AbstractFiterChain<T> implements FilterChain<T>{
	
	private static final ThreadLocal<Integer> COUNTER_LOCAL = new ThreadLocal<Integer>();
	
	private List<ChainFilter<T>> filters = new CopyOnWriteArrayList<>();

	@Override
	public void doFilter(T data) {
		Integer counter = COUNTER_LOCAL.get();
		if (counter == null) {
			counter = 0;
			COUNTER_LOCAL.set(counter);
		}else {
			COUNTER_LOCAL.set(++counter);
		}
		filters.get(counter).doFilter(data, this);
		counter = COUNTER_LOCAL.get();
		if (counter > 0) {
			COUNTER_LOCAL.set(--counter);
		}else {
			COUNTER_LOCAL.remove();
		}
	}

	@Override
	public void addFilter(ChainFilter<T> filter) {
		this.filters.add(filter);
	}

	@Override
	public void removeFilter(ChainFilter<T> filter) {
		this.filters.remove(filter);
		
	}
	
	

}
