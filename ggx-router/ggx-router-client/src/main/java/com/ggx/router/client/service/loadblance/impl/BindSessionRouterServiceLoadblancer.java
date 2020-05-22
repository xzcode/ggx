package com.ggx.router.client.service.loadblance.impl;

import java.util.Map;

import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGSession;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.loadblance.model.RouterServiceGroupLoadblanceInfo;
import com.ggx.router.client.service.loadblance.model.SessionBindRouterServiceInfo;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

/**
 * 默认路由服务负载均衡器
 *
 * @author zai 2020-05-06 11:48:17
 */
public class BindSessionRouterServiceLoadblancer implements RouterServiceLoadblancer {
	
	

	protected RouterServiceGroup routerServiceGroup;
	
	protected RouterServiceGroupLoadblanceInfo loadblanceInfo;
	
	public BindSessionRouterServiceLoadblancer() {
		this.loadblanceInfo = new RouterServiceGroupLoadblanceInfo();
	}

	@Override
	public GGFuture dispatch(Pack pack) {
		RouterService routerService = null;
		
		
		GGSession session = pack.getSession();
		
		if (session != null) {
			SessionBindRouterServiceInfo info = loadblanceInfo.getSessionBindRouterServiceInfo(session);
			if (info != null) {
				routerService = info.getRouterService();
				if (!routerService.isAvailable()) {
					loadblanceInfo.removeSessionBindRouterServiceInfo(session);
				}else {
					return routerService.dispatch(pack);
				}
			}
		}
		
		routerService = routerServiceGroup.getRandomRouterService();
		
		if (routerService == null) {
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		
		Map<String, RouterService> services = routerServiceGroup.getServices();
		if (services.size() == 0) {
			return null;
		}
		
		SessionBindRouterServiceInfo info = new SessionBindRouterServiceInfo(session, routerService);
		SessionBindRouterServiceInfo putIfAbsent = loadblanceInfo.putIfAbsentSessionBindRouterServiceInfo(session, info);
		if (putIfAbsent == null) {
			//session断开后移除关联
			session.addDisconnectListener(f -> {
				loadblanceInfo.removeSessionBindRouterServiceInfo(session);
			});
		}
		
		
		return routerService.dispatch(pack);
	}

	@Override
	public void setRouterServiceGroup(RouterServiceGroup routerServiceGroup) {
		this.routerServiceGroup = routerServiceGroup;
	}

}
