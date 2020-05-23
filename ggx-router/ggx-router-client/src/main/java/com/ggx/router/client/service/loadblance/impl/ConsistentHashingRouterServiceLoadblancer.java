package com.ggx.router.client.service.loadblance.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.GGXIdUtil;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.loadblance.impl.model.VirtualRouterServiceInfo;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

/**
 * 一致性哈希路由服务负载均衡器
 *
 * @author zai 2020-05-22 15:10:41
 */
public class ConsistentHashingRouterServiceLoadblancer implements RouterServiceLoadblancer {
	
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
	protected final Map<String, RouterService> sessionBindServiceCache = new ConcurrentSkipListMap<>();
	
	
	public ConsistentHashingRouterServiceLoadblancer(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	public GGFuture dispatch(Pack pack) {
		GGSession session = pack.getSession();
		RouterService routerService = this.sessionBindServiceCache.get(session.getSessonId());
		if (routerService != null) {
			if (routerService.isAvailable()) {
				return routerService.dispatch(pack);
			}
		}
		int sessionHash = getHash(pack.getSession().getSessonId());
		System.out.println(sessionHash);
		Integer firstKey = this.virtualRouterServices.ceilingKey(sessionHash);
		if (firstKey == null) {
			firstKey = this.virtualRouterServices.firstKey();
		}
		VirtualRouterServiceInfo serviceInfo = this.virtualRouterServices.get(firstKey);
		routerService = serviceInfo.getRouterService();
		
		RouterService putIfAbsent = this.sessionBindServiceCache.putIfAbsent(session.getSessonId(), routerService);
		if (putIfAbsent == null) {
			session.addDisconnectListener(s -> {
				this.sessionBindServiceCache.remove(session.getSessonId());
			});
		}
		
		return routerService.dispatch(pack);
	}

	/*
	 * 使用FNV1_32_HASH算法计算服务器的Hash值
	 */
	private static int getHash(String str) {
		final int p = 16777619;
		int hash = (int) 2166136261L;
		for (int i = 0; i < str.length(); i++)
			hash = (hash ^ str.charAt(i)) * p;
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;

		// 如果算出来的值为负数则取其绝对值
		if (hash < 0)
			hash = Math.abs(hash);
		return hash;
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
			this.virtualRouterServices.put(getHash(virtualServiceId), virtualRouterServiceInfo);
		}
		
	}
	private void removeService(RouterService routerService ) {
		String serviceId = routerService.getServiceId();
		for (int i = 0; i < VIRTUAL_ROUTER_SERVICE_SIZE; i++) {
			String virtualServiceId = serviceId + "#" + (i + 1);
			this.virtualRouterServices.remove(getHash(virtualServiceId));
			
		}
		
	}
	
	
	private static void hashTest() {
		//哈希分布测试
		ConcurrentSkipListMap<Integer, String> vMap = new ConcurrentSkipListMap<>();
		
		Map<String, AtomicInteger> countMap = new HashMap<>();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			String id = GGXIdUtil.newRandomStringId24();
			list.add(id);
			for (int j = 0; j < 100; j++) {
				String id2 = id  + "#" + (j+1);
				vMap.put(getHash(id2), id2);
				countMap.put(id, new AtomicInteger());
			}
		}
		int total = 10000 * 100;
		int hit = 0;
		for (int i = 0; i < total; i++) {
			String sessionId = GGXIdUtil.newRandomStringId24();
			int hash = getHash(sessionId);
			Integer ceilingKey = vMap.ceilingKey(hash);
			if (ceilingKey != null) {
				hit++;
				String id2 = vMap.get(ceilingKey);
				String id = id2.split("#")[0];
				countMap.get(id).getAndIncrement();
			}else {
				countMap.get(list.get(0)).getAndIncrement();
			}
			
		}
		
		//System.out.println(countMap);
		//System.out.println(1d * total / hit * 100D + "%");
		
	}

}
