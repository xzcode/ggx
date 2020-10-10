package com.ggx.router.client.service.loadblancer.model;

import com.ggx.core.common.session.GGXSession;
import com.ggx.router.client.service.RouterService;

/**
 * 会话与路由服务绑定信息
 *
 * @author zai
 * 2020-05-06 14:31:59
 */
public class SessionBindRouterServiceInfo {
	
	/**
	 * 会话对象
	 */
	protected GGXSession session;
	
	/**
	 * 路由服务
	 */
	protected RouterService routerService;
	
	
	

	public SessionBindRouterServiceInfo() {
		
	}

	public SessionBindRouterServiceInfo(GGXSession session, RouterService routerService) {
		this.session = session;
		this.routerService = routerService;
	}

	public GGXSession getSession() {
		return session;
	}

	public void setSession(GGXSession session) {
		this.session = session;
	}

	public RouterService getRouterService() {
		return routerService;
	}

	public void setRouterService(RouterService routerService) {
		this.routerService = routerService;
	}
	
	
	
	

}
