package com.ggx.rpc.client.service.loadbalancer;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.rpc.common.message.req.RpcReq;

/**
 * rpc服务负载均衡器
 * 
 * @author zai
 * 2020-10-3 13:32:33
 */
public interface RpcServiceLoadblancer {

	
	/**
	 * 负载均衡调用
	 * @param req
	 * @return
	 * @author zai
	 * 2020-10-3 13:32:43
	 */
	GGXFuture invoke(RpcReq req);

	
}
