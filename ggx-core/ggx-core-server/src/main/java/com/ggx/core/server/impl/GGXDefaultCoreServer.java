package com.ggx.core.server.impl;

import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.core.server.starter.GGXCoreServerStarter;
import com.ggx.core.server.starter.impl.DefaultGGXCoreServerStarter;

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
		if (!this.config.isInited()) {
			this.config.init();
		}
	}

	@Override
	public GGXFuture start() {
		this.shutdown();
		this.serverStarter = new DefaultGGXCoreServerStarter(config);
		return this.serverStarter.start();
	}

	@Override
	public GGXFuture shutdown() {
		if (this.serverStarter != null) {
			return this.serverStarter.shutdown();
		}
		return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
	}

	public GGXCoreServerConfig getConfig() {
		return config;
	}


}
