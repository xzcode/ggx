package com.ggx.core.common.filter.chain.impl;

import java.util.List;

import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.message.MessageData;

public class MessageFilterChain extends AbstractFiterChain<MessageData>{

	public MessageFilterChain(List<Filter<MessageData>> filters) {
		super(filters);
	}

}
