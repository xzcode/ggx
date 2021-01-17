package com.ggx.rpc.client.service.group;

import com.ggx.util.manager.impl.ListenableMapDataManager;

/**
 * RPC跨组服务组
 *
 * 2021-01-17 12:07:06
 */
public class RpcServiceCrossGroup extends ListenableMapDataManager<String, RpcServiceGroup>{
	
	//跨组服务组id
	protected String crossGroup;
	
	public RpcServiceCrossGroup(String crossGroup) {
		this.crossGroup = crossGroup;
	}

}
