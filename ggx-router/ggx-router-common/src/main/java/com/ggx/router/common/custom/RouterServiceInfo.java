package com.ggx.router.common.custom;

/**
 * 路由服务信息
 * 
 * @author zai
 * 2020-02-12 17:50:55
 */
public class RouterServiceInfo {
	
	//服务指令标识前缀
	protected String serviceActionIdPrefix;
	
	//路由组
	protected String routerGroup;
	
	//服务端口
	protected int servicePort;
	
	
	

	public String getServiceActionIdPrefix() {
		return serviceActionIdPrefix;
	}

	public void setServiceActionIdPrefix(String serviceActionIdPrefix) {
		this.serviceActionIdPrefix = serviceActionIdPrefix;
	}

	public String getRouterGroup() {
		return routerGroup;
	}

	public void setRouterGroup(String routerGroup) {
		this.routerGroup = routerGroup;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}
	
	
	
	

}
