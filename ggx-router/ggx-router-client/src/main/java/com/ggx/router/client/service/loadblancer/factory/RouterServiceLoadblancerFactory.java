package com.ggx.router.client.service.loadblancer.factory;

import com.ggx.router.client.service.loadblancer.RouterServiceLoadbalancer;

/**
 * 路由服务负载均衡器工厂接口
 *
 * @author zai
 * 2020-05-23 15:10:10
 */
public interface RouterServiceLoadblancerFactory {
	
	/**
	 * 获取负载均衡器
	 *
	 * @param type
	 * @return
	 * @author zai
	 * 2020-05-23 15:10:27
	 */
	RouterServiceLoadbalancer getLoadblancer(String type) ;

}
