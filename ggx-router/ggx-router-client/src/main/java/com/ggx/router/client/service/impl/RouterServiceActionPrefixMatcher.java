package com.ggx.router.client.service.impl;

import com.ggx.core.common.message.Pack;
import com.ggx.router.client.service.RouterServiceMatcher;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

/**
 * 默认action前缀路由服务匹配器
 * 
 * @author zai
 * 2019-11-07 17:29:02
 */
public class RouterServiceActionPrefixMatcher implements RouterServiceMatcher {
	

	@Override
	public boolean match(Pack pack, RouterServiceGroup routerServiceGroup) {
		return pack.getActionString().startsWith(routerServiceGroup.getActionIdPrefix());
	}

}
