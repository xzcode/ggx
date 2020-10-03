package com.ggx.rpc.client.service.loadbalancer.model;

import com.ggx.rpc.client.service.RpcService;

/**
 * 虚拟RPC服务信息
 *
 * @author zai
 * 2020-05-22 16:14:42
 */
public class VirtualRpcServiceInfo {
	
	/**
	 * 服务
	 */
	private RpcService rpcService;
	
	/**
	 * 虚拟服务id
	 */
	private String virtualServiceId;
	
	

	public VirtualRpcServiceInfo(RpcService rpcService, String virtualServiceId) {
		super();
		this.rpcService = rpcService;
		this.virtualServiceId = virtualServiceId;
	}

	

	public RpcService getRpcService() {
		return rpcService;
	}



	public void setRpcService(RpcService rpcService) {
		this.rpcService = rpcService;
	}



	public String getVirtualServiceId() {
		return virtualServiceId;
	}

	public void setVirtualServiceId(String virtualServiceId) {
		this.virtualServiceId = virtualServiceId;
	}
	
	

}
