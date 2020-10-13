package com.ggx.core.common.channel.handler.pack.impl;

import com.ggx.core.common.channel.handler.pack.ReceivePackHandler;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.task.ReceiveMessageTask;

public class DefaultReceivePackHandler implements ReceivePackHandler {
	

	private GGXCoreConfig config;
	
	public DefaultReceivePackHandler(GGXCoreConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void handle(Pack pack) {
		pack.getChannel().eventLoop().submit(new ReceiveMessageTask(pack, config));
	}


}
