package com.ggx.core.common.message.receive.impl;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.ReceiveMessageManager;

public class DefaultReceiveMessageManager implements ReceiveMessageManager {
	
	private GGXCoreConfig config;
	private FilterManager filterManager;
	
	public DefaultReceiveMessageManager(GGXCoreConfig config) {
		this.config = config;
		this.filterManager = this.config.getFilterManager();
	}

	@Override
	public void receive(MessageData data) {
		this.filterManager.doReceiveMessageFilters(data);
	}

	@Override
	public void receive(Pack pack) {
		this.filterManager.doReceivePackFilters(pack);
	}

}
