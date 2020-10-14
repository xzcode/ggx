package com.ggx.core.common.filter.chain.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.chain.FilterChain;

public abstract class AbstractFiterChain<T> implements FilterChain<T>{
	
	private ThreadLocal<Integer> COUNTER_LOCAL = new ThreadLocal<Integer>();
	
	private List<Filter<T>> filters = new CopyOnWriteArrayList<>();

	@Override
	public void doFilter(T data) throws Throwable{
		Integer counter = COUNTER_LOCAL.get();
		if (counter == null) {
			counter = 0;
		}else {
			counter++;
		}
		COUNTER_LOCAL.set(counter);
		
		if (filters.size() > counter) {
			try {
				filters.get(counter).doFilter(data, this);
			} catch (Throwable e) {
				throw e;
			}finally {
				counter = COUNTER_LOCAL.get();
				counter--;
				if (counter < 0) {
					COUNTER_LOCAL.remove();
				}else {
					COUNTER_LOCAL.set(counter);
				}
			}
		}
	}

	@Override
	public void addFilter(Filter<T> filter) {
		this.filters.add(filter);
	}

	@Override
	public void removeFilter(Filter<T> filter) {
		this.filters.remove(filter);
		
	}
	
	@Override
	public void setFilters(List<Filter<T>> filters) {
		this.filters = filters;
	}
	

}
