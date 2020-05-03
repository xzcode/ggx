package com.ggx.router.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.message.Pack;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.RouterServiceProvider;
import com.ggx.router.client.service.group.RouterServiceGroup;
import com.ggx.router.client.service.listener.AddRouterServiceListener;
import com.ggx.router.client.service.listener.RemoveRouterServiceListener;
import com.ggx.router.client.service.listener.RouterServiceListener;
import com.ggx.router.client.service.manager.RouterServiceManager;

/**
 * 默认路由服务提供者
 * 
 * @author zai
 * 2019-11-07 17:36:10
 */
public class DefaultServicePorvider implements RouterServiceProvider{
	
	protected RouterClientConfig config;
	
	
	protected List<AddRouterServiceListener> addRouterServiceListeners = new ArrayList<>();
	
	protected List<RemoveRouterServiceListener> removeRouterServiceListeners = new ArrayList<>();
	
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
	public RouterService matchService(Pack pack) {
		for (Entry<String, RouterService> entry : services.entrySet()) {
			RouterService service = entry.getValue();
			if (service.getServiceMatcher().match(pack)) {
				return service;
			}
		}
		return null;
	}


	@Override
	public void addListener(RouterServiceListener listener) {
		if (listener instanceof AddRouterServiceListener) {
			addRouterServiceListeners.add((AddRouterServiceListener) listener);
			return;
		}
		if (listener instanceof RemoveRouterServiceListener) {
			removeRouterServiceListeners.add((RemoveRouterServiceListener) listener);
			return;
		}
	}

	@Override
	public void removeListener(RouterServiceListener listener) {
		if (listener instanceof AddRouterServiceListener) {
			addRouterServiceListeners.remove((AddRouterServiceListener) listener);
			return;
		}
		if (listener instanceof RemoveRouterServiceListener) {
			removeRouterServiceListeners.remove((RemoveRouterServiceListener) listener);
			return;
		}
	}

	
}
