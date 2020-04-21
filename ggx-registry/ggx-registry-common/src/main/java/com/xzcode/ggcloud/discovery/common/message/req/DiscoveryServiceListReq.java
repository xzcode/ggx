package com.xzcode.ggcloud.discovery.common.message.req;

import com.ggx.core.common.message.model.IMessage;

public class DiscoveryServiceListReq  implements IMessage{
	
	public static final String ACTION = "GG.DISCOVERY.SERVICE.LIST.REQ";
	public static final DiscoveryServiceListReq DEFAULT_INSTANT = new DiscoveryServiceListReq();

	@Override
	public String getActionId() {
		return ACTION;
	}
	
}
