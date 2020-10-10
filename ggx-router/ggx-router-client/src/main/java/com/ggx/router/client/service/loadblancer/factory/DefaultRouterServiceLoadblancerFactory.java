package com.ggx.router.client.service.loadblancer.factory;

import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.loadblancer.RouterServiceLoadbalancer;
import com.ggx.router.client.service.loadblancer.constant.RouterServiceLoadblanceType;
import com.ggx.router.client.service.loadblancer.impl.BindSessionRouterServiceLoadbalancer;
import com.ggx.router.client.service.loadblancer.impl.ConsistentHashingRouterServiceLoadbalancer;
import com.ggx.router.client.service.loadblancer.impl.RandomRouterServiceLoadbalancer;

/**
 * 默认负载均衡器工厂实现类
 *
 * @author zai
 * 2020-05-23 15:11:25
 */
public class DefaultRouterServiceLoadblancerFactory implements RouterServiceLoadblancerFactory{
	
	
	/**
	 * 路由配置
	 */
	protected RouterClientConfig config;
	


	public DefaultRouterServiceLoadblancerFactory(RouterClientConfig config) {
		super();
		this.config = config;
	}



	public RouterServiceLoadbalancer getLoadblancer(String type) {
		switch (type) {
		case RouterServiceLoadblanceType.HASH:
			return new ConsistentHashingRouterServiceLoadbalancer(this.config.getSingleThreadTaskExecutor());
		case RouterServiceLoadblanceType.RANDOM_BIND:
			return new BindSessionRouterServiceLoadbalancer();
		case RouterServiceLoadblanceType.RANDOM:
			return new RandomRouterServiceLoadbalancer();

		default:
			return null;
		}
	}
}
