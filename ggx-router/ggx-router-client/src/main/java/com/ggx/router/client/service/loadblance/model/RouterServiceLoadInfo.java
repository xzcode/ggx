package com.ggx.router.client.service.loadblance.model;

import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.router.client.service.RouterService;

/**
 * 路由服务负载信息
 *
 * @author zai
 * 2020-05-06 17:03:28
 */
public class RouterServiceLoadInfo {
	
	private RouterService routerService;
	
	private AtomicInteger load = new AtomicInteger();
	
	public RouterServiceLoadInfo(RouterService routerService) {
		super();
		this.routerService = routerService;
	}

	public RouterService getRouterService() {
		return routerService;
	}
	
	public AtomicInteger getLoad() {
		return load;
	}

}
