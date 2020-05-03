package com.ggx.router.client.service;

import com.ggx.core.common.message.Pack;
import com.ggx.router.client.service.listener.RouterServiceListener;

/**
 * 路由解析器提供者
 * 
 * @author zai
 * 2019-10-12 14:57:37
 */
public interface RouterServiceProvider {
	
	/**
	 * 获取服务
	 * 
	 * @return
	 * @author zai
	 * 2019-10-12 15:01:39
	 */
	RouterService getService(String serviceGroupId, String serviceId);
	


	/**
	 * 移除路由服务
	 * 
	 * @param serviceName
	 * @return
	 * @author zai
	 * 2019-10-22 18:22:01
	 */
	void removeService(String serviceGroupId, String serviceId);
	
	/**
	 * 匹配服务
	 * 
	 * @param pack
	 * @return
	 * @author zai
	 * 2019-11-07 16:41:42
	 */
	RouterService matchService(Pack pack);

	/**
	 * 添加路由服务
	 * 
	 * @param serviceName
	 * @param service
	 * @return
	 * @author zai
	 * 2019-10-22 18:21:53
	 */
	void addService(RouterService service);
	
	/**
	 * 添加监听器
	 * 
	 * @param service
	 * @return
	 * @author zai
	 * 2019-11-07 17:14:55
	 */
	void addListener(RouterServiceListener listener);
	
	/**
	 * 移除监听器
	 * 
	 * @param service
	 * @return
	 * @author zai
	 * 2019-11-07 17:22:11
	 */
	void removeListener(RouterServiceListener listener);
	
}
