package com.ggx.router.client.service.loadblance.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGSession;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

/**
 * 默认路由服务负载均衡器
 *
 * @author zai 2020-05-06 11:48:17
 */
public class BindSessionRouterServiceLoadblancer implements RouterServiceLoadblancer {
	
	

	protected RouterServiceGroup routerServiceGroup;
	
	//会话与服务绑定集合
	protected final Map<String, RouterService> sessionBindServiceCache = new ConcurrentHashMap<>();

	
	public BindSessionRouterServiceLoadblancer() {
	}

	@Override
	public GGFuture dispatch(Pack pack) {
		RouterService routerService = null;
		
		
		GGSession session = pack.getSession();
		String sessonId = session.getSessonId();
		
		routerService = sessionBindServiceCache.get(sessonId);
		if (routerService != null) {
			if (!routerService.isAvailable()) {
				sessionBindServiceCache.remove(sessonId);
			}else {
				return routerService.dispatch(pack);
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
		
		RouterService putIfAbsent = sessionBindServiceCache.putIfAbsent(sessonId, routerService);
		if (putIfAbsent == null) {
			//session断开后移除关联
			session.addDisconnectListener(f -> {
				sessionBindServiceCache.remove(sessonId);
			});
		}
		
		return routerService.dispatch(pack);
		
	}

	@Override
	public void setRouterServiceGroup(RouterServiceGroup routerServiceGroup) {
		this.routerServiceGroup = routerServiceGroup;
	}
	
	@Override
	public void changeSessionBinding(String sessionId, RouterService routerService) {
		this.sessionBindServiceCache.replace(sessionId, routerService);
		
	}

}
