package com.ggx.rpc.client.service.cache;

import java.util.concurrent.ConcurrentSkipListMap;

import com.ggx.rpc.client.service.RpcService;
import com.ggx.rpc.client.service.group.RpcServiceGroup;
import com.ggx.rpc.client.service.loadbalancer.model.VirtualRpcServiceInfo;
import com.ggx.util.hash.HashUtil;

/**
 * 一致性哈希RPC服务缓存
 * @author zai
 *
 */
public class HashedRpcServiceCache{
	
	
	/**
	 * 虚拟路由服务数量
	 */
	protected static final int VIRTUAL_ROUTER_SERVICE_SIZE = 100;
	
	/**
	 * 虚拟路由服务集合
	 */
	protected final ConcurrentSkipListMap<Integer, VirtualRpcServiceInfo> virtualRpcServices = new ConcurrentSkipListMap<>();
	
	
	protected RpcServiceGroup serviceGroup;
	
	public HashedRpcServiceCache(RpcServiceGroup serviceGroup) {
		this.serviceGroup = serviceGroup;
		
		this.serviceGroup.onPut((key, rpcService) -> {
			addService(rpcService);
		});
		
		this.serviceGroup.onRemove((key, rpcService) -> {
			removeService(rpcService);
		});
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

	public RpcService getService(String key) {
		int hashKey = HashUtil.getFNV1_32_HASH(key);
		
		Integer firstKey = this.virtualRpcServices.ceilingKey(hashKey);
		if (firstKey == null && this.virtualRpcServices.size() > 0) {
			firstKey = this.virtualRpcServices.firstKey();
		}
		VirtualRpcServiceInfo serviceInfo = this.virtualRpcServices.get(firstKey);
		RpcService rpcService = serviceInfo.getRpcService();
		return rpcService;
	}


}
