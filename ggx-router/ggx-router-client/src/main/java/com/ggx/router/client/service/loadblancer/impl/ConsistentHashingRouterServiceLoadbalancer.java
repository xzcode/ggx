package com.ggx.router.client.service.loadblancer.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.loadblancer.RouterServiceLoadbalancer;
import com.ggx.router.client.service.loadblancer.impl.model.VirtualRouterServiceInfo;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;
import com.ggx.util.hash.HashUtil;

/**
 * 一致性哈希路由服务负载均衡器
 *
 * @author zai 2020-05-22 15:10:41
 */
public class ConsistentHashingRouterServiceLoadbalancer implements RouterServiceLoadbalancer {
	
	/**
	 * 任务执行器
	 */
	protected TaskExecutor taskExecutor;
	
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
	
	
	public ConsistentHashingRouterServiceLoadbalancer(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	public GGXFuture dispatch(Pack pack) {
		
		GGXSession session = pack.getSession();
		String sessonId = session.getSessonId();
		
		RouterService routerService = this.sessionBindServiceCache.get(sessonId);
		if (routerService != null) {
			if (routerService.isAvailable()) {
				return routerService.dispatch(pack);
			}
		}
		int sessionHash = HashUtil.getFNV1_32_HASH(pack.getSession().getSessonId());
		
		Integer firstKey = this.virtualRouterServices.ceilingKey(sessionHash);
		if (firstKey == null) {
			firstKey = this.virtualRouterServices.firstKey();
		}
		VirtualRouterServiceInfo serviceInfo = this.virtualRouterServices.get(firstKey);
		routerService = serviceInfo.getRouterService();
		
		RouterService putIfAbsent = this.sessionBindServiceCache.putIfAbsent(sessonId, routerService);
		if (putIfAbsent == null) {
			session.addDisconnectListener(s -> {
				this.sessionBindServiceCache.remove(sessonId);
			});
		}
		
		return routerService.dispatch(pack);
	}
	
	

	@Override
	public void setRouterServiceGroup(RouterServiceGroup routerServiceGroup) {
		
		this.routerServiceGroup = routerServiceGroup;
		
		this.routerServiceGroup.addAddRouterServiceListener(routerService -> {
			taskExecutor.submitTask(() -> {
				addService(routerService);
			});
		});
		
		this.routerServiceGroup.addRemoveRouterServiceListener(routerService -> {
			taskExecutor.submitTask(() -> {
				removeService(routerService);
			});
			
		});
	}
	
	private void addService(RouterService routerService ) {
		String serviceId = routerService.getServiceId();
		for (int i = 0; i < VIRTUAL_ROUTER_SERVICE_SIZE; i++) {
			String virtualServiceId = serviceId + "#" + (i + 1);
			VirtualRouterServiceInfo virtualRouterServiceInfo = new VirtualRouterServiceInfo(routerService, virtualServiceId);
			this.virtualRouterServices.put(HashUtil.getFNV1_32_HASH(virtualServiceId), virtualRouterServiceInfo);
		}
		
	}
	private void removeService(RouterService routerService ) {
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
	
	/*
	 * private static void hashTest() { //哈希分布测试 ConcurrentSkipListMap<Integer,
	 * String> vMap = new ConcurrentSkipListMap<>();
	 * 
	 * Map<String, AtomicInteger> countMap = new HashMap<>(); List<String> list =
	 * new ArrayList<String>(); for (int i = 0; i < 5; i++) { String id =
	 * GGXIdUtil.newRandomStringId24(); list.add(id); for (int j = 0; j < 100; j++)
	 * { String id2 = id + "#" + (j+1); vMap.put(HashUtil.getFNV1_32_HASH(id2), id2);
	 * countMap.put(id, new AtomicInteger()); } } int total = 10000 * 100; for (int
	 * i = 0; i < total; i++) { String sessionId = GGXIdUtil.newRandomStringId24();
	 * int hash = HashUtil.getFNV1_32_HASH(sessionId); Integer ceilingKey = vMap.ceilingKey(hash); if
	 * (ceilingKey != null) { String id2 = vMap.get(ceilingKey); String id =
	 * id2.split("#")[0]; countMap.get(id).getAndIncrement(); }else {
	 * countMap.get(list.get(0)).getAndIncrement(); }
	 * 
	 * }
	 * 
	 * //System.out.println(countMap); //System.out.println(1d * total / hit * 100D
	 * + "%");
	 * 
	 * }
	 */

}
