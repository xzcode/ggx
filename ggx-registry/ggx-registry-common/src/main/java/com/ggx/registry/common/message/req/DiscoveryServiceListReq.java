package com.ggx.registry.common.message.req;

import com.ggx.core.common.message.model.Message;

public class DiscoveryServiceListReq  implements Message{
	
	public static final String ACTION = "GG.DISCOVERY.SERVICE.LIST.REQ";
	public static final DiscoveryServiceListReq DEFAULT_INSTANT = new DiscoveryServiceListReq();

	@Override
	public String getActionId() {
		return ACTION;
	}
	
}
