package com.ggx.core.common.filter;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.model.Message;

public interface FilterChain {
	
	void doFilter(MessageData<? extends Message> data);

}
