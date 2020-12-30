package com.ggx.core.common.filter.chain.impl;

import java.util.List;

import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.message.Pack;

public class PackFilterChain extends AbstractFiterChain<Pack>{

	public PackFilterChain(List<Filter<Pack>> filters) {
		super(filters);
	}

}
