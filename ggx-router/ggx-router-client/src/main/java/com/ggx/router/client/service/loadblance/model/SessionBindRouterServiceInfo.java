package com.ggx.router.client.service.loadblance.model;

import com.ggx.core.common.session.GGSession;
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
	protected GGSession session;
	
	/**
	 * 路由服务
	 */
	protected RouterService routerService;
	
	
	

	public SessionBindRouterServiceInfo() {
		
	}

	public SessionBindRouterServiceInfo(GGSession session, RouterService routerService) {
		this.session = session;
		this.routerService = routerService;
	}

	public GGSession getSession() {
		return session;
	}

	public void setSession(GGSession session) {
		this.session = session;
	}

	public RouterService getRouterService() {
		return routerService;
	}

	public void setRouterService(RouterService routerService) {
		this.routerService = routerService;
	}
	
	
	
	

}
