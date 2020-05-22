package com.ggx.router.client.service.loadblance.impl.model;

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
	private String serviceId;
	
	/**
	 * 虚拟服务id
	 */
	private String virtualServiceId;
	
	

	public VirtualRouterServiceInfo(String serviceId, String virtualServiceId) {
		super();
		this.serviceId = serviceId;
		this.virtualServiceId = virtualServiceId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getVirtualServiceId() {
		return virtualServiceId;
	}

	public void setVirtualServiceId(String virtualServiceId) {
		this.virtualServiceId = virtualServiceId;
	}
	
	

}
