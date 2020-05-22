package com.ggx.router.client.service.loadblance.impl;

import java.util.TreeMap;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.message.Pack;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.group.RouterServiceGroup;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.loadblance.impl.model.VirtualRouterServiceInfo;
import com.ggx.router.client.service.manager.RouterServiceManager;

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
	 * 路由客户端配置
	 */
	protected RouterClientConfig config;
	
	/**
	 * 路由服务管理器
	 */
	protected RouterServiceManager routerServiceManager;
	
	/**
	 * 虚拟路由服务集合
	 */
	protected final TreeMap<String, VirtualRouterServiceInfo> virtualRouterServices = new TreeMap<>();
	
	
	
	public ConsistentHashingRouterServiceLoadblancer(TaskExecutor taskExecutor, RouterClientConfig config) {
		this.taskExecutor = taskExecutor;
		this.config = config;
		this.routerServiceManager = this.config.getRouterServiceManager();
	}

	@Override
	public RouterService getRouterService(Pack pack, RouterServiceGroup routerServiceGroup) {
		RouterService routerService = null;
		
		return routerService;
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

}
