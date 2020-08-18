package com.ggx.router.client.service.impl;

import java.util.Map.Entry;

import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.RouterServiceMatcher;
import com.ggx.router.client.service.RouterServiceProvider;
import com.ggx.router.client.service.manager.RouterServiceManager;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

/**
 * 默认路由服务提供者
 * 
 * @author zai
 * 2019-11-07 17:36:10
 */
public class DefaultServicePorvider implements RouterServiceProvider{
	
	protected RouterClientConfig config;
	
	protected RouterServiceManager routerServiceManager;
	
	public DefaultServicePorvider(RouterClientConfig config) {
		this.config = config;
	}

	@Override
	public RouterService getService(String serviceGroupId, String serviceId) {
		RouterServiceGroup group = routerServiceManager.getServiceGroup(serviceGroupId);
		if (group != null) {
			return group.getService(serviceId);
		}
		return null;
	}

	@Override
	public void addService(RouterService service) {
		routerServiceManager.addService(service);
	}

	@Override
	public void removeService(String serviceGroupId, String serviceId) {
		routerServiceManager.removeService(serviceGroupId, serviceId);
	}

	@Override
	public GGFuture dispatch(Pack pack) {
		for (Entry<String, RouterServiceGroup> entry : this.config.getRouterServiceManager().getServiceGroups().entrySet()) {
			RouterServiceMatcher routerServiceMatcher = this.config.getRouterServiceMatcher();
			RouterServiceGroup group = entry.getValue();
			if (routerServiceMatcher.match(pack, group)) {
				//通过负载均衡策略，获取适用的服务对象，进行返回
				return group.dispatch(pack);
			}
		}
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}


	
}
