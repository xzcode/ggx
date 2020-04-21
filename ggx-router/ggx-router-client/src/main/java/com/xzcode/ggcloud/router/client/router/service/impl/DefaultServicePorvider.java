package com.xzcode.ggcloud.router.client.router.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.message.Pack;
import com.xzcode.ggcloud.router.client.config.RouterClientConfig;
import com.xzcode.ggcloud.router.client.router.service.IRouterService;
import com.xzcode.ggcloud.router.client.router.service.IRouterServiceProvider;
import com.xzcode.ggcloud.router.client.router.service.listener.IAddRouterServiceListener;
import com.xzcode.ggcloud.router.client.router.service.listener.IRemoveRouterServiceListener;
import com.xzcode.ggcloud.router.client.router.service.listener.IRouterServiceListener;

/**
 * 默认路由服务提供者
 * 
 * @author zai
 * 2019-11-07 17:36:10
 */
public class DefaultServicePorvider implements IRouterServiceProvider{
	
	protected RouterClientConfig config;
	
	
	protected List<IAddRouterServiceListener> addRouterServiceListeners = new ArrayList<>();
	
	protected List<IRemoveRouterServiceListener> removeRouterServiceListeners = new ArrayList<>();
	
	protected Map<String, IRouterService> services = new ConcurrentHashMap<>(100);
	
	public DefaultServicePorvider(RouterClientConfig config) {
		this.config = config;
	}

	@Override
	public IRouterService getService(String serviceId) {
		return services.get(serviceId);
	}

	@Override
	public IRouterService addService(IRouterService service) {
		return services.put(service.getServiceId(), service);
	}

	@Override
	public IRouterService removeService(String serviceId) {
		return services.remove(serviceId);
	}

	@Override
	public IRouterService matchService(Pack pack) {
		for (Entry<String, IRouterService> entry : services.entrySet()) {
			IRouterService service = entry.getValue();
			if (service.getServiceMatcher().match(pack)) {
				return service;
			}
		}
		return null;
	}


	@Override
	public void addListener(IRouterServiceListener listener) {
		if (listener instanceof IAddRouterServiceListener) {
			addRouterServiceListeners.add((IAddRouterServiceListener) listener);
			return;
		}
		if (listener instanceof IRemoveRouterServiceListener) {
			removeRouterServiceListeners.add((IRemoveRouterServiceListener) listener);
			return;
		}
	}

	@Override
	public void removeListener(IRouterServiceListener listener) {
		if (listener instanceof IAddRouterServiceListener) {
			addRouterServiceListeners.remove((IAddRouterServiceListener) listener);
			return;
		}
		if (listener instanceof IRemoveRouterServiceListener) {
			removeRouterServiceListeners.remove((IRemoveRouterServiceListener) listener);
			return;
		}
	}

	
}
