package com.xzcode.ggserver.core.server.impl;

import com.ggx.core.common.future.GGFuture;
import com.xzcode.ggserver.core.server.GGXCoreServer;
import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;
import com.xzcode.ggserver.core.server.starter.GGXCoreServerStarter;
import com.xzcode.ggserver.core.server.starter.impl.DefaultGGXCoreServerStarter;

/**
 * 默认服务器实现
 * 
 * @author zai
 * 2019-12-05 10:40:41
 */
public class GGXDefaultCoreServer implements GGXCoreServer {

	private GGXCoreServerConfig config;

	private GGXCoreServerStarter serverStarter;
	

	public GGXDefaultCoreServer(GGXCoreServerConfig serverConfig) {
		this.config = serverConfig;
	}

	@Override
	public GGFuture start() {
		this.shutdown();
		this.serverStarter = new DefaultGGXCoreServerStarter(config);
		return this.serverStarter.start();
	}

	@Override
	public void shutdown() {
		if (this.serverStarter != null) {
			this.serverStarter.shutdown();
		}
	}

	@Override
	public GGXCoreServerConfig getConfig() {
		return config;
	}

}
