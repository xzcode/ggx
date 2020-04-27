package com.ggx.router.client.router.service;

import com.ggx.core.common.message.Pack;

/**
 * 服务匹配器
 * 
 * @author zai
 * 2019-11-07 16:49:15
 */
public interface IRouterServiceMatcher {
	
	/**
	 * 进行服务匹配
	 * 
	 * @param pack 数据包
	 * @return 是否匹配
	 * @author zai
	 * 2019-11-07 16:52:31
	 */
	boolean match(Pack pack);
	
}
