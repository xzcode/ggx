package com.ggx.router.client.service.loadblance.constant;

/**
 * 路由服务负载均衡器类型
 *
 * @author zai
 * 2020-05-22 18:01:01
 */
public interface RouterServiceLoadblanceType {
	
	/**
	 * 完全随机
	 */
	String RANDOM = "random";
	
	/**
	 * 绑定第一次随机结果
	 */
	String RANDOM_BIND = "random_bind";
	
	/**
	 * 一致性哈希策略
	 */
	String HASH = "hash";
	
}
