package com.ggx.router.client.service.impl;

import com.ggx.core.common.message.Pack;
import com.ggx.router.client.service.RouterServiceMatcher;

/**
 * 默认action前缀路由服务匹配器
 * 
 * @author zai
 * 2019-11-07 17:29:02
 */
public class RouterServiceActionPrefixMatcher implements RouterServiceMatcher {
	
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
