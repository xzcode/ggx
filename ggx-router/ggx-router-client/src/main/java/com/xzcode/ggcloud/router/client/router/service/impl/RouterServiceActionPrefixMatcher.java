package com.xzcode.ggcloud.router.client.router.service.impl;

import com.xzcode.ggcloud.router.client.router.service.IRouterServiceMatcher;
import com.xzcode.ggserver.core.common.message.Pack;

/**
 * 默认action前缀路由服务匹配器
 * 
 * @author zai
 * 2019-11-07 17:29:02
 */
public class RouterServiceActionPrefixMatcher implements IRouterServiceMatcher {
	
	private String prefix;
	

	public RouterServiceActionPrefixMatcher(String prefix) {
		super();
		this.prefix = prefix;
	}




	@Override
	public boolean match(Pack pack) {
		return pack.getActionString().startsWith(prefix);
	}
	
	public String getPrefix() {
		return prefix;
	}

}
