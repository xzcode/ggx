package com.ggx.router.client.service.impl;

import com.ggx.core.common.message.Pack;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.router.client.service.RouterServiceMatcher;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;

/**
 * 默认注册中心相关actionid前缀路由服务匹配器
 * 
 * @author zai
 * 2020-02-07 11:34:00
 */
public class DefaultRegistryRouterServiceActionPrefixMatcher implements RouterServiceMatcher {
	
	private String prefix;
	

	public DefaultRegistryRouterServiceActionPrefixMatcher(ServiceInfo service) {
		prefix = service.getCustomData().get(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX);
	}




	@Override
	public boolean match(Pack pack) {
		return pack.getActionString().startsWith(prefix);
	}

}
