package com.ggx.router.client.service.loadblance.constant;

/**
 * 路由服务提供者类型
 * 
 * @author zai
 * 2020-9-28 21:51:02
 */
public interface RouterServiceProviderType {
	
	/**
	 * 单一服务
	 */
	String REGISTRY_SINGLE_SERVICE = "registry_single_service";
	
	/**
	 * 多服务
	 */
	String REGISTRY_MULTI_SERVICES = "registry_multi_services";
	
	
	String DEFAULT = "default";
	
}
