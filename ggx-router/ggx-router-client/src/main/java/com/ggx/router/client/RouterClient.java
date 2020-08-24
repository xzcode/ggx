package com.ggx.router.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.router.client.config.RouterClientConfig;

/**
 * 路由客户端
 * 
 * 
 * @author zai
 * 2019-10-03 14:04:16
 */
public class RouterClient{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RouterClient.class);
	
	private RouterClientConfig config;
	
	public RouterClient(RouterClientConfig config) {
		this.config = config;
		this.config.setRouterClient(this);
		this.config.init();
	}
	
	public GGFuture route(Pack pack) {
		try {
				return config.getServiceProvider().dispatch(pack);
		} catch (Exception e) {
			LOGGER.error("Route Message Error!", e);
		}
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}

	public RouterClientConfig getConfig() {
		return config;
	}
	
	public void shutdown() {
		this.config.getSharedEventLoopGroup().shutdownGracefully();
	}

}
