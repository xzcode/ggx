package com.ggx.router.client.service.listener;

import com.ggx.router.client.service.RouterService;

/**
 * 路由服务监听器
 * 
 * @author zai
 * 2019-11-07 17:19:12
 */
public interface RouterServiceListener {
	
	/**
	 * 触发处理函数
	 * 
	 * @param routerService
	 * @author zai
	 * 2019-11-07 17:19:22
	 */
	void trigger(RouterService routerService);
	
}
