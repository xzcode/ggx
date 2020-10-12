package com.ggx.core.common.channel.handler.pack.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.channel.handler.pack.ReceivePackHandler;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.task.MessageDataTask;

public class DefaultReceivePackHandler implements ReceivePackHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultReceivePackHandler.class);

	private GGXCoreConfig config;
	
	public DefaultReceivePackHandler(GGXCoreConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void handle(Pack pack) {
		pack.getChannel().eventLoop().submit(new MessageDataTask(pack, config));
	}


}