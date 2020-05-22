package com.ggx.router.client.service.loadblance.factory;

import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.loadblance.constant.RouterServiceLoadblanceType;
import com.ggx.router.client.service.loadblance.impl.BindSessionRouterServiceLoadblancer;
import com.ggx.router.client.service.loadblance.impl.ConsistentHashingRouterServiceLoadblancer;
import com.ggx.router.client.service.loadblance.impl.RandomRouterServiceLoadblancer;

public class RouterServiceLoadblancerFactory {
	
	
	/**
	 * 路由配置
	 */
	protected RouterClientConfig config;
	


	public RouterServiceLoadblancerFactory(RouterClientConfig config) {
		super();
		this.config = config;
	}



	public RouterServiceLoadblancer getLoadblancer(String type) {
		switch (type) {
		case RouterServiceLoadblanceType.HASH:
			return new ConsistentHashingRouterServiceLoadblancer(this.config.getSingleThreadTaskExecutor());
		case RouterServiceLoadblanceType.RANDOM_BIND:
			return new BindSessionRouterServiceLoadblancer();
		case RouterServiceLoadblanceType.RANDOM:
			return new RandomRouterServiceLoadblancer();

		default:
			return null;
		}
	}
}
