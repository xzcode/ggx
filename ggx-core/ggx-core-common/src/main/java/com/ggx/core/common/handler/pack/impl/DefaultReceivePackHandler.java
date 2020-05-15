package com.ggx.core.common.handler.pack.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.handler.pack.IReceivePackHandler;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.task.MessageDataTask;

public class DefaultReceivePackHandler implements IReceivePackHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultReceivePackHandler.class);

	private GGConfig config;
	
	public DefaultReceivePackHandler(GGConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void handle(Pack pack) {
		pack.getChannel().eventLoop().submit(new MessageDataTask(pack, config));
	}


}
