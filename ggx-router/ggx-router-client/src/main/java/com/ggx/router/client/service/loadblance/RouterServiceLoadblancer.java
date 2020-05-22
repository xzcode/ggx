package com.ggx.router.client.service.loadblance;

import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

/**
 * 路由服务负载均衡器
 *
 * @author zai
 * 2020-05-03 04:01:45
 */
public interface RouterServiceLoadblancer {

	
	/**
	 * 通过负载均衡策略获取路由服务
	 *
	 * @return
	 * @author zai
	 * 2020-05-06 11:38:52
	 */
	GGFuture dispatch(Pack pack);

	void setRouterServiceGroup(RouterServiceGroup routerServiceGroup);
	
}
