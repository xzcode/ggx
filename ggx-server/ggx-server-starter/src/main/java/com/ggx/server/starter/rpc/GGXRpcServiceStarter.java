package com.ggx.server.starter.rpc;

import com.ggx.core.common.config.GGXCore;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.rpc.client.RpcClient;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.server.RpcServer;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.server.starter.basic.GGXBasicServerStarter;

public class GGXRpcServiceStarter extends GGXBasicServerStarter {


	public void init() {
		
		if (this.registryClientConfig == null) {
			this.registryClientConfig = new RegistryClientConfig();
		}
		this.registryClient = new RegistryClient(registryClientConfig);

		if (this.rpcClientConfig == null) {
			this.rpcClientConfig = new RpcClientConfig();
		}
		this.rpcClientConfig.setRegistryClient(registryClient);
		this.rpcClient = new RpcClient(rpcClientConfig);
		
		if (this.rpcServerConfig == null) {
			this.rpcServerConfig = new RpcServerConfig();
		}
		this.rpcServerConfig.setRegistryClient(registryClient);
		this.rpcServer = new RpcServer(rpcServerConfig);

	}
	

	@Override
	public void start() {
		this.rpcServer.start().addListener(f -> {
			if (f.isSuccess()) {
				this.registryClient.start();
			}else {
				this.shutdown();
			}
		});
	}
	
	
	
	
	@Override
	public GGXCore getGGXCore() {
		return this.registryClientConfig.getCoreClient();
	}

}
