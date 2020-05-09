package com.ggx.router.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.RouterService;

/**
 * 默认网关路由器
 * 
 * 
 * @author zai
 * 2019-10-03 14:04:16
 */
public class DefaultRouterClient implements RouterClient{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRouterClient.class);
	
	private RouterClientConfig config;
	
	public DefaultRouterClient(RouterClientConfig config) {
		this.config = config;
		this.config.setRouterClient(this);
		this.config.init();
	}
	
	@Override
	public GGFuture route(Pack pack) {
		try {
			//进行服务匹配
			RouterService matchService = config.getServiceProvider().matchService(pack);
			if (matchService != null) {
				return matchService.dispatch(pack);
			}
		} catch (Exception e) {
			LOGGER.error("Route Message Error!", e);
		}
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}

}
