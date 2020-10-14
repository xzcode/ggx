package com.ggx.core.common.filter.impl;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.filter.chain.FilterChain;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.model.Message;

public class FinalReceiveMessageChainFilter implements ReceiveMessageFilter{

	
	private GGXCoreConfig config;

	public FinalReceiveMessageChainFilter(GGXCoreConfig config) {
		this.config = config;
	}
	
	@Override
	public void doFilter(MessageData data, FilterChain<MessageData> filterChain) throws Throwable {
		Object returnObject = config.getMessageControllerManager().invoke(data);
		if (returnObject != null && returnObject instanceof Message) {
			data.getSession().send((Message)returnObject);
		}
	}

}
