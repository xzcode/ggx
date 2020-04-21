package com.xzcode.ggcloud.router.client.router.service.impl;

import com.ggx.core.common.message.Pack;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;
import com.xzcode.ggcloud.router.client.router.service.IRouterServiceMatcher;
import com.xzcode.ggcloud.router.common.constant.RouterServiceCustomDataKeys;

/**
 * 默认注册中心相关actionid前缀路由服务匹配器
 * 
 * @author zai
 * 2020-02-07 11:34:00
 */
public class DefaultDiscoveryRouterServiceActionPrefixMatcher implements IRouterServiceMatcher {
	
	private String prefix;
	

	public DefaultDiscoveryRouterServiceActionPrefixMatcher(ServiceInfo service) {
		prefix = service.getCustomData().get(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX);
	}




	@Override
	public boolean match(Pack pack) {
		return pack.getActionString().startsWith(prefix);
	}

}
