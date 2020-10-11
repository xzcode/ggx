package com.ggx.core.common.filter.chain;

import com.ggx.core.common.message.MessageData;

public interface FilterChain {
	
	void doFilter(MessageData data);

}
