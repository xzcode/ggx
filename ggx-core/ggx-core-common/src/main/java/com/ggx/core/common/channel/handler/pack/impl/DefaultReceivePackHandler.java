package com.ggx.core.common.channel.handler.pack.impl;

import com.ggx.core.common.channel.handler.pack.ReceivePackHandler;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.ReceiveMessageManager;

public class DefaultReceivePackHandler implements ReceivePackHandler {

	protected GGXCoreConfig config;

	private ReceiveMessageManager receiveMessageManager;

	public DefaultReceivePackHandler(GGXCoreConfig config) {
		this.config = config;
		this.receiveMessageManager = config.getReceiveMessageManager();
	}

	@Override
	public void handle(Pack pack) {
		this.receiveMessageManager.receive(pack);
	}

}
