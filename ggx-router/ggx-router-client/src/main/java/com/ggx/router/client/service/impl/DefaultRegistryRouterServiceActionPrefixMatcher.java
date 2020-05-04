package com.ggx.router.client.service.impl;

import com.ggx.core.common.message.Pack;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.router.client.service.RouterServiceMatcher;
import com.ggx.router.client.service.group.RouterServiceGroup;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;

/**
 * 默认注册中心相关actionid前缀路由服务匹配器
 * 
 * @author zai
 * 2020-02-07 11:34:00
 */
public class DefaultRegistryRouterServiceActionPrefixMatcher implements RouterServiceMatcher {
	


	@Override
	public boolean match(Pack pack, RouterServiceGroup routerServiceGroup) {
		return pack.getActionString().startsWith(prefix);
	}

}
