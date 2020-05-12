package com.ggx.router.client.service.loadblance.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGSession;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.group.RouterServiceGroup;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.loadblance.model.RouterServiceGroupLoadblanceInfo;
import com.ggx.router.client.service.loadblance.model.SessionBindRouterServiceInfo;

/**
 * 默认路由服务负载均衡器
 *
 * @author zai 2020-05-06 11:48:17
 */
public class DefaultRouterServiceLoadblancer implements RouterServiceLoadblancer {
	
	
	
	protected final Map<RouterServiceGroup, RouterServiceGroupLoadblanceInfo> sessionBindServiceInfos = new ConcurrentHashMap<>(100);

	@Override
	public RouterService getRouterService(Pack pack, RouterServiceGroup routerServiceGroup) {
		
		RouterServiceGroupLoadblanceInfo newLoadblanceInfo = sessionBindServiceInfos.get(routerServiceGroup);
		if (newLoadblanceInfo == null) {
			newLoadblanceInfo = new RouterServiceGroupLoadblanceInfo();
			RouterServiceGroupLoadblanceInfo putIfAbsent = sessionBindServiceInfos.putIfAbsent(routerServiceGroup, newLoadblanceInfo);
			if (putIfAbsent != null) {
				newLoadblanceInfo = putIfAbsent;
			}
		}
		
		RouterServiceGroupLoadblanceInfo loadblanceInfo = newLoadblanceInfo;
		
		GGSession session = pack.getSession();
		
		if (session != null) {
			SessionBindRouterServiceInfo info = loadblanceInfo.getSessionBindRouterServiceInfo(session);
			if (info != null) {
				RouterService routerService = info.getRouterService();
				if (!routerService.isAvailable()) {
					loadblanceInfo.removeSessionBindRouterServiceInfo(session);
				}
			}
		}
		
		RouterService routerService = routerServiceGroup.getLowLoadingRouterService();
		
		if (routerService == null) {
			return null;
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
		
		
		return routerService;
	}

}
