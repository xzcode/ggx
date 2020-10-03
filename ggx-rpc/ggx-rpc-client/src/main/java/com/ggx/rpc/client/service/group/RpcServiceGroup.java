package com.ggx.rpc.client.service.group;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.rpc.client.service.RpcService;
import com.ggx.rpc.client.service.loadbalancer.RpcServiceLoadblancer;
import com.ggx.rpc.common.message.req.RpcReq;
import com.ggx.util.manager.impl.ListenableMapDataManager;

/**
 * rpc服务组
 * 
 * @author zai
 * 2020-10-2 14:54:39
 */
public class RpcServiceGroup extends ListenableMapDataManager<String, RpcService>{
	
	//rpc组id
	protected String serviceGroupId;
	
	protected RpcServiceLoadblancer loadblancer;
	
	
	
	
	public RpcServiceGroup(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}
	public RpcServiceGroup(String serviceGroupId, RpcServiceLoadblancer loadblancer) {
		this.serviceGroupId = serviceGroupId;
		this.loadblancer = loadblancer;
	}




	public GGXFuture invoke(RpcReq req) {
		if (this.loadblancer != null) {
			return this.loadblancer.invoke(req);
		}
		RpcService randomOne = this.getRandomOne();
		return randomOne.invoke(req);

	}
	
	
	public String getServiceGroupId() {
		return serviceGroupId;
	}

}
