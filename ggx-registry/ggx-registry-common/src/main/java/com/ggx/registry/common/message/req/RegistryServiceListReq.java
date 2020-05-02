package com.ggx.registry.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.constant.RegistryConstant;

public class RegistryServiceListReq  implements Message{
	
	public static final String ACTION_ID = RegistryConstant.ACTION_ID_PREFIX + ".SERVICE.LIST.REQ";
	public static final RegistryServiceListReq DEFAULT_INSTANT = new RegistryServiceListReq();

	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
}
