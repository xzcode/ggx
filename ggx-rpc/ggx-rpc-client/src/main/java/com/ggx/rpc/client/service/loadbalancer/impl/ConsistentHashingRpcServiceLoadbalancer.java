package com.ggx.rpc.client.service.loadbalancer.impl;

import java.util.concurrent.ConcurrentSkipListMap;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.rpc.client.service.RpcService;
import com.ggx.rpc.client.service.group.RpcServiceGroup;
import com.ggx.rpc.client.service.loadbalancer.RpcServiceLoadbalancer;
import com.ggx.rpc.client.service.loadbalancer.model.VirtualRpcServiceInfo;
import com.ggx.rpc.common.message.req.RpcReq;
import com.ggx.util.hash.HashUtil;

/**
 * 一致性哈希路由服务负载均衡器
 *
 * @author zai 2020-05-22 15:10:41
 */
public class ConsistentHashingRpcServiceLoadbalancer implements RpcServiceLoadbalancer {
	
	
	/**
	 * 虚拟路由服务数量
	 */
	protected static final int VIRTUAL_ROUTER_SERVICE_SIZE = 100;
	
	/**
	 * 虚拟路由服务集合
	 */
	protected final ConcurrentSkipListMap<Integer, VirtualRpcServiceInfo> virtualRpcServices = new ConcurrentSkipListMap<>();
	
	
	protected RpcServiceGroup serviceGroup;
	
	public ConsistentHashingRpcServiceLoadbalancer(RpcServiceGroup serviceGroup) {
		this.serviceGroup = serviceGroup;
		
		this.serviceGroup.onPut(rpcService -> {
			addService(rpcService);
		});
		
		this.serviceGroup.onRemove(rpcService -> {
			removeService(rpcService);
		});
	}

	@Override
	public GGXFuture<?> invoke(RpcReq req) {
		
		String rpcId = req.getRpcId();
		
		int sessionHash = HashUtil.getFNV1_32_HASH(rpcId);
		
		Integer firstKey = this.virtualRpcServices.ceilingKey(sessionHash);
		if (firstKey == null && this.virtualRpcServices.size() > 0) {
			firstKey = this.virtualRpcServices.firstKey();
		}
		VirtualRpcServiceInfo serviceInfo = this.virtualRpcServices.get(firstKey);
		RpcService rpcService = serviceInfo.getRpcService();
		
		return rpcService.invoke(req);
	}
	
	public void addService(RpcService rpcService ) {
		String serviceId = rpcService.getServiceId();
		for (int i = 0; i < VIRTUAL_ROUTER_SERVICE_SIZE; i++) {
			String virtualServiceId = serviceId + "#" + (i + 1);
			VirtualRpcServiceInfo virtualRpcServiceInfo = new VirtualRpcServiceInfo(rpcService, virtualServiceId);
			this.virtualRpcServices.put(HashUtil.getFNV1_32_HASH(virtualServiceId), virtualRpcServiceInfo);
		}
		
	}
	public void removeService(RpcService rpcService ) {
		String serviceId = rpcService.getServiceId();
		for (int i = 0; i < VIRTUAL_ROUTER_SERVICE_SIZE; i++) {
			String virtualServiceId = serviceId + "#" + (i + 1);
			this.virtualRpcServices.remove(HashUtil.getFNV1_32_HASH(virtualServiceId));
			
		}
		
	}


}
