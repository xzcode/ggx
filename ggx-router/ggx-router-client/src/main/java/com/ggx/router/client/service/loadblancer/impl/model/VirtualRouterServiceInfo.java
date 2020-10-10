package com.ggx.router.client.service.loadblancer.impl.model;

import com.ggx.router.client.service.RouterService;

/**
 * 虚拟路由服务信息
 *
 * @author zai
 * 2020-05-22 16:14:42
 */
public class VirtualRouterServiceInfo {
	
	/**
	 * 服务id
	 */
	private RouterService routerService;
	
	/**
	 * 虚拟服务id
	 */
	private String virtualServiceId;
	
	

	public VirtualRouterServiceInfo(RouterService routerService, String virtualServiceId) {
		super();
		this.routerService = routerService;
		this.virtualServiceId = virtualServiceId;
	}

	

	public RouterService getRouterService() {
		return routerService;
	}



	public void setRouterService(RouterService routerService) {
		this.routerService = routerService;
	}



	public String getVirtualServiceId() {
		return virtualServiceId;
	}

	public void setVirtualServiceId(String virtualServiceId) {
		this.virtualServiceId = virtualServiceId;
	}
	
	

}
