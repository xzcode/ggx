package com.ggx.server.gateway;

import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.xzcode.ggserver.core.server.GGXCoreServer;
import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;
import com.xzcode.ggserver.core.server.impl.GGXDefaultCoreServer;

public class GGXGateway {
	
	protected GGXCoreServer coreServer;
	protected GGXCoreServerConfig coreServerConfig;
	
	
	protected RegistryClientConfig registryClientConfig;
	protected RegistryClient registryClient;
	
	protected RouterClientConfig routerClientConfig;
	protected RouterClient routerClient;
	/**
	 * 初始化
	 *
	 * @author zzz
	 * 2020-08-21 18:26:28
	 */
	public void init() {
		
		
		RegistryClient registryClient = new RegistryClient(registryClientConfig);
		
		this.coreServerConfig = new GGXCoreServerConfig();
		this.coreServerConfig.init();
		
		this.coreServer = new GGXDefaultCoreServer(coreServerConfig);
		
		this.routerClientConfig = new RouterClientConfig(coreServer);
		this.routerClientConfig.setHostServer(coreServer);
		this.routerClientConfig.setRegistryClient(registryClient);
		
		this.routerClient = new RouterClient(routerClientConfig);
		
	}
	
	
	/**
	 * 启动
	 *
	 * @author zzz
	 * 2020-08-21 18:24:48
	 */
	public void start() {
		
	}

}
