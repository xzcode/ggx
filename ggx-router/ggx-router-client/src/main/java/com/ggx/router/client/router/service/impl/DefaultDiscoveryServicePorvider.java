package com.ggx.router.client.router.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.message.Pack;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.router.service.IRouterService;
import com.ggx.router.client.router.service.IRouterServiceProvider;
import com.ggx.router.client.router.service.listener.IAddRouterServiceListener;
import com.ggx.router.client.router.service.listener.IRemoveRouterServiceListener;
import com.ggx.router.client.router.service.listener.IRouterServiceListener;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;

/**
 * 默认路由服务提供者
 * 
 * @author zai
 * 2019-11-07 17:36:10
 */
public class DefaultDiscoveryServicePorvider implements IRouterServiceProvider{
	
	/**
	 * 路由客户端配置
	 */
	protected RouterClientConfig config;
	
	/**
	 * 服务管理器
	 */
	protected ServiceManager serviceManager;
	
	/**
	 * 添加路由服务监听器
	 */
	protected List<IAddRouterServiceListener> addRouterServiceListeners = new ArrayList<>();
	
	/**
	 * 移除路由服务监听器
	 */
	protected List<IRemoveRouterServiceListener> removeRouterServiceListeners = new ArrayList<>();
	
	/**
	 * 服务集合<服务id,路由服务对象>
	 */
	protected Map<String, IRouterService> services = new ConcurrentHashMap<>();
	
	/**
	 * actionId对应路由服务缓存,<actionId,路由服务对象>
	 */
	protected Map<String, IRouterService> actionServiceCache = new ConcurrentHashMap<>();
	
	
	public DefaultDiscoveryServicePorvider(RouterClientConfig config) {
		RegistryClient discoveryClient = config.getRegistryClient();
		this.config = config;
		this.serviceManager = discoveryClient.getConfig().getServiceManager();
		
		//添加连接注册中心成功回调
		discoveryClient.addRegisterSuccessListener(() -> {
			for (Entry<String, IRouterService> entry : services.entrySet()) {
				IRouterService routerService = entry.getValue();
				if (!routerService.isAvailable()) {
					removeService(routerService.getServiceId());
				}
			}
		});
		
		//添加注册中心服务管器的服务注册监听器
		this.serviceManager.addRegisterListener(service -> {
			//注册路由服务
			registerRouterService(service);
		});
		
		//添加注册中心服务管器的服务取消注册监听器
		this.serviceManager.addUnregisterListener(service -> {
			removeService(service.getServiceId());
		});
		
		//添加注册中心服务管器的服务更新监听器
		this.serviceManager.addUpdateListener(service -> {
			
			IRouterService routerService = getService(service.getServiceId());
			if (routerService != null) {
				routerService.addAllExtraData(service.getCustomData());
			}else {
				registerRouterService(service);
			}
			
		});
		
	}
	
	/**
	 * 注册路由服务
	 * 
	 * @param service
	 * @author zai
	 * 2020-02-06 18:24:17
	 */
	private void registerRouterService(ServiceInfo service) {
		Map<String, String> customData = service.getCustomData();
		String routerGroup = customData.get(RouterServiceCustomDataKeys.ROUTER_SERVICE_GROUP);
		if (!config.getRouterGroupId().equals(routerGroup)) {
			return;
		}
		String actionIdPrefix = customData.get(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX);
		String servicePortString = customData.get(RouterServiceCustomDataKeys.ROUTER_SERVICE_PORT);
		if (actionIdPrefix == null || servicePortString == null) {
			return;
		}
		Integer servicePort = Integer.valueOf(servicePortString);
		
		String serviceId = service.getServiceId();
		
		//检查是否存在id一样的旧服务
		IRouterService oldService = getService(serviceId);
		if (oldService != null) {
			RouterServiceActionPrefixMatcher serviceMatcher = (RouterServiceActionPrefixMatcher) oldService.getServiceMatcher();
			if (!actionIdPrefix.equals(serviceMatcher.getPrefix())) {
				//移除信息不一致的旧服务
				removeService(serviceId);
			}
			else if (servicePort != oldService.getPort()) {
				//移除端口信息不一致的旧服务
				removeService(serviceId);
			}
			else if (!oldService.isAvailable()) {
				//移除不可用的旧服务
				removeService(serviceId);
			}else {
				return;
			}
		}
		
		
		//创建新服务对象
		DefaultRouterService routerService = new DefaultRouterService(config, serviceId);
        routerService.setHost(service.getHost());
        routerService.setPort(servicePort);
        routerService.setServiceId(service.getServiceId());
        routerService.setServcieName(service.getServiceName());
        routerService.addAllExtraData(service.getCustomData());
        routerService.setServiceMatcher(new RouterServiceActionPrefixMatcher(actionIdPrefix));
        addService(routerService);
        
        routerService.init();
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
		IRouterService service = services.remove(serviceId);
		if (service != null) {
			service.shutdown();
			removeActionServiceCache(service);
		}
		return service;
	}
	
	private void removeActionServiceCache(IRouterService service) {
		Iterator<String> it = actionServiceCache.keySet().iterator();
		RouterServiceActionPrefixMatcher serviceMatcher = (RouterServiceActionPrefixMatcher) service.getServiceMatcher();
		while (it.hasNext()) {
			String actionId  = it.next();
			if (actionId.startsWith(serviceMatcher.getPrefix())) {
				it.remove();
			}
		}
	}

	@Override
	public IRouterService matchService(Pack pack) {
		String actionId = pack.getActionString();
		//尝试从缓存中获取服务
		IRouterService service = actionServiceCache.get(actionId);
		if (service != null) {
			return service;
		}
		//遍历进行服务匹配
		for (Entry<String, IRouterService> entry : services.entrySet()) {
			service = entry.getValue();
			if (service.getServiceMatcher().match(pack)) {
				actionServiceCache.put(actionId, service);
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
