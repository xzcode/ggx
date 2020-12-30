package com.ggx.core.common.filter.chain.impl;

import java.util.List;

import com.ggx.core.common.filter.Filter;

public class DefaultFilterChain<T> extends AbstractFiterChain<T>{

	public DefaultFilterChain(List<Filter<T>> filters) {
		super(filters);
	}

}
