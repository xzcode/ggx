package com.ggx.router.client.service.loadblancer.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.executor.SingleThreadTaskExecutor;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.loadblancer.RouterServiceLoadbalancer;
import com.ggx.router.client.service.loadblancer.impl.model.VirtualRouterServiceInfo;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;
import com.ggx.util.hash.HashUtil;
import com.ggx.util.thread.GGXThreadFactory;

/**
 * 一致性哈希路由服务负载均衡器
 *
 * @author zai 2020-05-22 15:10:41
 */
public class ConsistentHashingRouterServiceLoadbalancer implements RouterServiceLoadbalancer {
	
	/**
	 * 共享任务执行器
	 */
	protected static final TaskExecutor TASK_EXECUTOR = new SingleThreadTaskExecutor(new GGXThreadFactory("C-HASH-LB-", true));
	
	/**
	 * 路由服务管理器
	 */
	protected RouterServiceGroup routerServiceGroup;
	
	/**
	 * 虚拟路由服务数量
	 */
	protected static final int VIRTUAL_ROUTER_SERVICE_SIZE = 100;
	
	/**
	 * 虚拟路由服务集合
	 */
	protected final ConcurrentSkipListMap<Integer, VirtualRouterServiceInfo> virtualRouterServices = new ConcurrentSkipListMap<>();
	
	/**
	 * 会话与路由绑定缓存
	 */
	protected final Map<String, RouterService> sessionBindServiceCache = new ConcurrentHashMap<>();
	
	
	public ConsistentHashingRouterServiceLoadbalancer() {
	}

	@Override
	public GGXFuture<?> loadblance(Pack pack, String serviceId) {
		
		GGXSession session = pack.getSession();
		String sessonId = session.getSessionId();
		
		RouterService routerService = this.sessionBindServiceCache.get(sessonId);
		if (routerService != null) {
			if (routerService.isAvailable()) {
				return routerService.dispatch(pack);
			}
		}
		
		if (serviceId == null) {
			if (this.virtualRouterServices.size() == 0) {
				return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
			}
			int sessionHash = HashUtil.getFNV1_32_HASH(pack.getSession().getSessionId());
			
			Integer firstKey = this.virtualRouterServices.ceilingKey(sessionHash);
			if (firstKey == null) {
				firstKey = this.virtualRouterServices.firstKey();
			}
			VirtualRouterServiceInfo serviceInfo = this.virtualRouterServices.get(firstKey);
			routerService = serviceInfo.getRouterService();
		}else {
			routerService = routerServiceGroup.getService(serviceId);
		}
		
		if (routerService == null) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		
		RouterService putIfAbsent = this.sessionBindServiceCache.putIfAbsent(sessonId, routerService);
		if (putIfAbsent == null) {
			session.addDisconnectListener(s -> {
				this.sessionBindServiceCache.remove(sessonId);
			});
		}
		
		return routerService.dispatch(pack);
	}
	
	@Override
	public GGXFuture<?> loadblance(Pack pack) {
		return this.loadblance(pack, null);
	}
	

	@Override
	public void setRouterServiceGroup(RouterServiceGroup routerServiceGroup) {
		
		this.routerServiceGroup = routerServiceGroup;
		
		this.routerServiceGroup.addAddRouterServiceListener(routerService -> {
			TASK_EXECUTOR.submitTask(() -> {
				addService(routerService);
			});
		});
		
		this.routerServiceGroup.addRemoveRouterServiceListener(routerService -> {
			TASK_EXECUTOR.submitTask(() -> {
				removeService(routerService);
			});
			
		});
	}
	
	protected void addService(RouterService routerService ) {
		String serviceId = routerService.getServiceId();
		for (int i = 0; i < VIRTUAL_ROUTER_SERVICE_SIZE; i++) {
			String virtualServiceId = serviceId + "#" + (i + 1);
			VirtualRouterServiceInfo virtualRouterServiceInfo = new VirtualRouterServiceInfo(routerService, virtualServiceId);
			this.virtualRouterServices.put(HashUtil.getFNV1_32_HASH(virtualServiceId), virtualRouterServiceInfo);
		}
		
	}
	protected void removeService(RouterService routerService ) {
		String serviceId = routerService.getServiceId();
		for (int i = 0; i < VIRTUAL_ROUTER_SERVICE_SIZE; i++) {
			String virtualServiceId = serviceId + "#" + (i + 1);
			this.virtualRouterServices.remove(HashUtil.getFNV1_32_HASH(virtualServiceId));
			
		}
		
	}

	@Override
	public void changeSessionBinding(String sessionId, RouterService routerService) {
		this.sessionBindServiceCache.replace(sessionId, routerService);
	}

}
