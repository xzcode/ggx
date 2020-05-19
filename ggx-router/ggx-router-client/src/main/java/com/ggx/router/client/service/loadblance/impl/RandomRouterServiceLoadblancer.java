package com.ggx.router.client.service.loadblance.impl;

import com.ggx.core.common.message.Pack;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.group.RouterServiceGroup;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;

/**
 * 随机路由服务负载均衡器
 *
 * @author zai
 * 2020-05-19 11:59:20
 */
public class RandomRouterServiceLoadblancer implements RouterServiceLoadblancer {
	
	

	@Override
	public RouterService getRouterService(Pack pack, RouterServiceGroup routerServiceGroup) {
		return routerServiceGroup.getRandomRouterService();
	}

}
