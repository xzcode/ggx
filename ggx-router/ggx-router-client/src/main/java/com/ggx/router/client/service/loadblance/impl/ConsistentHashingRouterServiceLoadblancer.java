package com.ggx.router.client.service.loadblance.impl;

import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
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
	
	
	public ConsistentHashingRouterServiceLoadblancer(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	public GGFuture dispatch(Pack pack) {
		RouterService routerService = null;
		Integer firstKey = this.virtualRouterServices.ceilingKey(getHash(pack.getSession().getSessonId()));
		if (firstKey == null) {
			firstKey = this.virtualRouterServices.firstKey();
		}
		VirtualRouterServiceInfo serviceInfo = this.virtualRouterServices.get(firstKey);
		routerService = serviceInfo.getRouterService();
		return routerService.dispatch(pack);
	}

	/*
	 * 使用FNV1_32_HASH算法计算服务器的Hash值
	 */
	private int getHash(String str) {
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

}
