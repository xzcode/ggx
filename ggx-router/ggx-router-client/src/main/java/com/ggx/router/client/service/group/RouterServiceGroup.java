package com.ggx.router.client.service.group;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.RouterServiceMatcher;

/**
 * 路由服务组
 *
 * @author zai
 * 2020-05-03 03:05:36
 */
public class RouterServiceGroup {
	
	/**
	 * 服务组id
	 */
	protected String serviceGroupId;
	
	/**
	 * 服务集合Map<服务id, 服务对象>
	 */
	protected final Map<String, RouterService> services = new ConcurrentHashMap<>();
	
	/**
	 * 服务匹配器
	 */
	protected RouterServiceMatcher serviceMatcher;
	
	

	public RouterServiceGroup(String serviceGroupId) {
		super();
		this.serviceGroupId = serviceGroupId;
	}
	
	
	public void addService(RouterService service) {
		this.services.put(service.getServiceId(), service);
	}
	
	
	public RouterService getService(String serviceId) {
		return this.services.get(serviceId);
	}
	
	/**
	 * 移除服务
	 *
	 * @param serviceId
	 * @author zai
	 * 2020-05-04 17:39:11
	 */
	public void reomoveService(String serviceId) {
		RouterService routerService = this.services.remove(serviceId);
		if (routerService != null) {
			routerService.shutdown();
		}
	}
	
	public String getServiceGroupId() {
		return serviceGroupId;
	}
	
	public void setServiceGroupId(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}
	
	public RouterServiceMatcher getServiceMatcher() {
		return serviceMatcher;
	}
	
	public void setServiceMatcher(RouterServiceMatcher serviceMatcher) {
		this.serviceMatcher = serviceMatcher;
	}
	
	public Map<String, RouterService> getServices() {
		return services;
	}

}
