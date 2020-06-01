package com.ggx.router.client.service.loadblance.impl;

import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

/**
 * 随机路由服务负载均衡器
 *
 * @author zai
 * 2020-05-19 11:59:20
 */
public class RandomRouterServiceLoadblancer implements RouterServiceLoadblancer {
	
	protected RouterServiceGroup routerServiceGroup;
	
	

	@Override
	public GGFuture dispatch(Pack pack) {
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
