package com.ggx.core.common.message.send.impl;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.send.SendMessageManager;

public class DefaultSendMessageManager implements SendMessageManager{
	
	private GGXCoreConfig config;
	private FilterManager filterManager;
	
	public DefaultSendMessageManager(GGXCoreConfig config) {
		this.config = config;
		this.filterManager = this.config.getFilterManager();
	}

	@Override
	public GGXFuture send(MessageData data) {
		return this.filterManager.doSendMessageFilters(data);
	}

	@Override
	public GGXFuture send(Pack pack) {
		return this.filterManager.doSendPackFilters(pack);
	}

}
