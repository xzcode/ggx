package com.ggx.router.client.service.listener;

import com.ggx.router.client.service.RouterService;

/**
 * 路由服务关闭监听器
 *
 * @author zai
 * 2020-05-06 15:11:00
 */
public interface RouterServiceShutdownListener {
	
	/**
	 * 触发关闭监听
	 *
	 * @param routerService
	 * @author zai
	 * 2020-05-06 15:11:12
	 */
	void onShutdown(RouterService routerService);
	
}
