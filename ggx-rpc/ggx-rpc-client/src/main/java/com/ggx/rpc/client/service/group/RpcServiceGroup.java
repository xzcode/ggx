package com.ggx.rpc.client.service.group;

import com.ggx.rpc.client.service.RpcService;
import com.ggx.util.manager.impl.ListenableMapDataManager;

/**
 * rpc服务组
 * 
 * @author zai
 * 2020-10-2 14:54:39
 */
public class RpcServiceGroup extends ListenableMapDataManager<String, RpcService>{
	
	//rpc组id
	protected String rpcGroupId;
	

}
