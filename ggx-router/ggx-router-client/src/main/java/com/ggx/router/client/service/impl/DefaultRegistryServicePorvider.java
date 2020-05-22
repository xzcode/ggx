package com.ggx.router.client.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.RouterServiceProvider;
import com.ggx.router.client.service.listener.AddRouterServiceListener;
import com.ggx.router.client.service.listener.RemoveRouterServiceListener;
import com.ggx.router.client.service.listener.RouterServiceListener;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.manager.RouterServiceManager;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;

/**
 * 默认路由服务提供者
 * 
 * @author zai
 * 2019-11-07 17:36:10
 */
public class DefaultRegistryServicePorvider implements RouterServiceProvider{
	
	/**
	 * 路由客户端配置
	 */
	protected RouterClientConfig config;
	

	
	/**
	 * 路由服务管理器
	 */
	protected RouterServiceManager routerServiceManager;
	
	
	/**
	 * actionId对应路由服务组缓存,<actionId,路由服务组对象>
	 */
	protected Map<String, RouterServiceGroup> actionServiceCache = new ConcurrentHashMap<>();
	
	
	/**
	 * 构造函数
	 * @param config
	 */
	public DefaultRegistryServicePorvider(RouterClientConfig config) {
		this.config = config;
		this.routerServiceManager = config.getRouterServiceManager();
		init();
	}
	
	
	private void init() {
		
		RegistryClient registryClient = this.config.getRegistryClient();
		
		ServiceManager serviceManager = registryClient.getConfig().getServiceManager();
		
		//添加本服务注册成功回调
		registryClient.addRegisterSuccessListener(() -> {
			
			//移除所有不可用的路由服务
			this.routerServiceManager.removeAllUnavaliableRouterServices();
			
		});
		
		//添加注册中心服务管器的服务注册监听器
		serviceManager.addRegisterListener(service -> {
			//注册路由服务
			registerRouterService(service);
		});
		
		//添加注册中心服务管器的服务取消注册监听器
		serviceManager.addUnregisterListener(service -> {
			removeService(service.getServiceGroupId(), service.getServiceId());
		});
		
		//添加注册中心服务管器的服务更新监听器
		serviceManager.addUpdateListener(service -> {
			
			RouterService routerService = getService(service.getServiceGroupId(), service.getServiceId());
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
		
		//获取自定义参数
		Map<String, String> customData = service.getCustomData();
		String routerGroup = customData.get(RouterServiceCustomDataKeys.ROUTER_GROUP_ID);
		if (!config.getRouterGroupId().equals(routerGroup)) {
			return;
		}
		String actionIdPrefix = customData.get(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX);
		String servicePortString = customData.get(RouterServiceCustomDataKeys.ROUTER_SERVICE_PORT);
		if (actionIdPrefix == null || servicePortString == null) {
			return;
		}
		Integer servicePort = Integer.valueOf(servicePortString);
		
		String serviceGroupId = service.getServiceGroupId();
		
		String serviceId = service.getServiceId();
		
		RouterServiceGroup serviceGroup = this.routerServiceManager.getServiceGroup(serviceGroupId);
		
		if (serviceGroup != null) {
			
				//检查是否存在id一样的旧服务
				RouterService oldService = serviceGroup.getService(serviceId);
				if (oldService != null) {
					
					if (!actionIdPrefix.equals(serviceGroup.getActionIdPrefix())) {
						//移除信息不一致的旧服务
						removeService(serviceGroupId, serviceId);
					}
					else if (servicePort != oldService.getPort()) {
						//移除端口信息不一致的旧服务
						removeService(serviceGroupId, serviceId);
					}
					else if (!oldService.isAvailable()) {
						//移除不可用的旧服务
						removeService(serviceGroupId, serviceId);
					}else {
						return;
					}
				}
			}
			
			//创建新服务对象
			RouterService routerService = new RouterService(config, serviceId);
	        routerService.setHost(service.getHost());
	        routerService.setPort(servicePort);
	        routerService.setServiceId(service.getServiceId());
	        routerService.setServiceGroupId(service.getServiceGroupId());
	        routerService.setActionIdPrefix(actionIdPrefix);
	        routerService.setServiceName(service.getServiceName());
	        routerService.addAllExtraData(service.getCustomData());
	        
	        this.routerServiceManager.addService(routerService);
	        
	        /*
	        //添加服务关闭监听器
	        routerService.addShutdownListener(s -> {
	        	RouterServiceLoadblancer loadblancer = this.config.getRouterServiceLoadblancer();
	        	if (loadblancer != null) {
	        		
				}
	        });
	        */
	        routerService.init();
        
		
	}

	@Override
	public RouterService getService(String serviceGroupId, String serviceId) {
		return this.routerServiceManager.getService(serviceGroupId, serviceId);
	}

	@Override
	public void addService(RouterService service) {
		this.routerServiceManager.addService(service);
	}

	@Override
	public void removeService(String serviceGroupId, String serviceId) {
		this.routerServiceManager.removeService(serviceGroupId, serviceId);
	}
	
	private void removeActionServiceCache(RouterServiceGroup serviceGroup) {
		Iterator<String> it = actionServiceCache.keySet().iterator();
		while (it.hasNext()) {
			String actionId  = it.next();
			if (actionId.startsWith(serviceGroup.getActionIdPrefix())) {
				it.remove();
			}
		}
	}

	@Override
	public GGFuture dispatch(Pack pack) {
		String actionId = pack.getActionString();
		
		//尝试从缓存中获取服务
		RouterServiceGroup routerServiceGroup = actionServiceCache.get(actionId);
		if (routerServiceGroup != null) {
			
			
			return routerServiceGroup.dispatch(pack);
		}
		
		Map<String, RouterServiceGroup> serviceGroups = this.routerServiceManager.getServiceGroups();
		
		//遍历进行服务匹配
		for (Entry<String, RouterServiceGroup> entry : serviceGroups.entrySet()) {
			routerServiceGroup = entry.getValue();
			if (routerServiceGroup != null) {
				//匹配服务组
				boolean match = this.config.getRouterServiceMatcher().match(pack, routerServiceGroup);
				if (match) {
					//添加服务组到缓存
					actionServiceCache.put(actionId, routerServiceGroup);
					
					return routerServiceGroup.dispatch(pack);
				}
			}
		}
		
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}

}
