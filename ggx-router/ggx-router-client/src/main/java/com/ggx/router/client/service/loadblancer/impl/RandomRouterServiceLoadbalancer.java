package com.ggx.router.client.service.loadblancer.impl;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.GGXFailedFuture;
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
	public GGXFuture<?> loadblance(Pack pack, String serviceId) {
		if (serviceId == null) {
			return routerServiceGroup.dispatchRandom(pack);
		}
		RouterService service = routerServiceGroup.getService(serviceId);
		if (service == null) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		return service.dispatch(pack);
	}

	@Override
	public GGXFuture<?> loadblance(Pack pack) {
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
