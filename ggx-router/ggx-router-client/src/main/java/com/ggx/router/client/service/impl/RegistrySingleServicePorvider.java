package com.ggx.router.client.service.impl;

import java.util.Map;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.RouterServiceProvider;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;

/**
 * 单路由服务提供者
 * 
 * @author zai
 * 2019-11-07 17:36:10
 */
public class RegistrySingleServicePorvider implements RouterServiceProvider{
	
	/**
	 * 路由客户端配置
	 */
	protected RouterClientConfig config;
	
	
	/**
	 * 路由服务组
	 */
	protected RouterServiceGroup routerServiceGroup;
	
	
	/**
	 * 构造函数
	 * @param config
	 */
	public RegistrySingleServicePorvider(RouterClientConfig config) {
		this.config = config;
		this.routerServiceGroup = new RouterServiceGroup(this.config.getRouterGroupId(),this.config.getRouterServiceLoadblancer());
		init();
	}
	
	
	private void init() {
		
		RegistryClient registryClient = this.config.getRegistryClient();
		
		ServiceManager serviceManager = registryClient.getConfig().getServiceManager();
		
		//添加本服务注册成功回调
		registryClient.addRegisterSuccessListener(() -> {
			
			//移除所有不可用的路由服务
			this.routerServiceGroup.removeAllUnavaliableRouterServices();
			
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
		if (servicePortString == null) {
			return;
		}
		
		int load = Integer.valueOf(customData.get(RouterServiceCustomDataKeys.ROUTER_SERVICE_LOAD));
		
		Integer servicePort = Integer.valueOf(servicePortString);
		
		String serviceGroupId = service.getServiceGroupId();
		
		String serviceId = service.getServiceId();
		
		
			
		//检查是否存在id一样的旧服务
		RouterService oldService = routerServiceGroup.getService(serviceId);
		if (oldService != null) {
			
			if (!actionIdPrefix.equals(routerServiceGroup.getActionIdPrefix())) {
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
			
			//创建新服务对象
			RouterService routerService = new RouterService(config, serviceId);
			routerService.setLoad(load);
	        routerService.setHost(service.getHost());
	        routerService.setPort(servicePort);
	        routerService.setServiceId(service.getServiceId());
	        routerService.setServiceGroupId(service.getServiceGroupId());
	        routerService.setActionIdPrefix(actionIdPrefix);
	        routerService.setServiceName(service.getServiceName());
	        routerService.addAllExtraData(service.getCustomData());
	        
	        this.routerServiceGroup.addService(routerService);
	        
	        routerService.init();
        
		
	}

	@Override
	public RouterService getService(String serviceGroupId, String serviceId) {
		return this.routerServiceGroup.getService(serviceId);
	}

	@Override
	public void addService(RouterService service) {
		this.routerServiceGroup.addService(service);
	}

	@Override
	public void removeService(String serviceGroupId, String serviceId) {
		this.routerServiceGroup.removeService(serviceId);
	}
	

	@Override
	public GGXFuture<?>  dispatch(Pack pack) {
		return routerServiceGroup.dispatch(pack);
	}


	@Override
	public RouterServiceGroup getDefaultRouterServiceGroup() {
		return this.routerServiceGroup;
	}

}
