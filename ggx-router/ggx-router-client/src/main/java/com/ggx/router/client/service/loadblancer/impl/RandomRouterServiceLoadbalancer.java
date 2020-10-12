package com.ggx.router.client.service.loadblancer.impl;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.loadblancer.RouterServiceLoadbalancer;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

/**
 * 随机路由服务负载均衡器
 *
 * @author zai
 * 2020-05-19 11:59:20
 */
public class RandomRouterServiceLoadbalancer implements RouterServiceLoadbalancer {
	
	protected RouterServiceGroup routerServiceGroup;
	
	

	@Override
	public GGXFuture dispatch(Pack pack) {
		return routerServiceGroup.dispatchRandom(pack);
	}

	@Override
	public void setRouterServiceGroup(RouterServiceGroup routerServiceGroup) {
		this.routerServiceGroup = routerServiceGroup;
	}


	@Override
	public void changeSessionBinding(String sessionId, RouterService routerService) {
		
	}

}