package com.ggx.router.client.service.listener;

import com.ggx.router.client.service.RouterService;

/**
 * 路由服务移除监听器
 *
 * @author zai
 * 2020-05-03 22:35:30
 */
public interface RouterServiceOnRemoveListener {
	
	void onRemove(RouterService routerService);
	
}
