package com.ggx.server.gateway;

import java.util.List;

import com.ggx.core.common.utils.GGXIdUtil;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.client.registry.RegistryInfo;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.server.GGXServer;
import com.xzcode.ggserver.core.server.GGXCoreServer;
import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;
import com.xzcode.ggserver.core.server.impl.GGXDefaultCoreServer;

public class GGXGateway implements GGXServer{
	
	protected String serviceName = "Gateway";
	protected String serviceId = GGXIdUtil.newRandomStringId24();
	protected String serviceDescName = "Gateway";
	
	
	protected String serviceGroupId = "gateway-group-01";
	protected String serviceGroupDescName = "Gateway";
	
	protected String routerGroupId = "gateway-group-01";
	
	protected List<RegistryInfo> registries;
	
	protected boolean changeAndRebootIfPortInUse = true;
	protected int port = 10001;
	protected String websocketPath = "/websocket";
	protected String portChangeStrategy = "increment";
	protected boolean sessionDisconnectTransferReuestEnabled = true;
	protected boolean sessionDisconnectTransferResponseEnabled = false;
	
	
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
	private void init() {
		
		this.coreServerConfig = new GGXCoreServerConfig();
		this.coreServerConfig.setPort(port);
		this.coreServerConfig.setWebsocketPath(websocketPath);	
		this.coreServerConfig.setChangeAndRebootIfPortInUse(changeAndRebootIfPortInUse);
		this.coreServerConfig.setPortChangeStrategy(portChangeStrategy);
		
		this.coreServerConfig.init();
		
		this.coreServer = new GGXDefaultCoreServer(coreServerConfig);
		
		this.registryClientConfig  = new RegistryClientConfig();
		this.registryClientConfig.setServiceName(serviceName);
		this.registryClientConfig.setServiceDescName(serviceDescName);
		
		this.registryClientConfig.setServiceGroupId(routerGroupId);
		this.registryClientConfig.setServiceGroupDescName(serviceGroupDescName);
		
		this.registryClient = new RegistryClient(registryClientConfig);
		
		
		this.routerClientConfig = new RouterClientConfig(coreServer);
		this.routerClientConfig.setHostServer(coreServer);
		this.routerClientConfig.setRegistryClient(registryClient);
		this.routerClientConfig.setRouterGroupId(routerGroupId);
		this.routerClientConfig.setSessionDisconnectTransferRequestEnabled(sessionDisconnectTransferReuestEnabled);
		this.routerClientConfig.setSessionDisconnectTransferResponseEnabled(sessionDisconnectTransferResponseEnabled);
		
		this.routerClient = new RouterClient(routerClientConfig);
		
	}
	
	
	/**
	 * 启动
	 *
	 * @author zzz
	 * 2020-08-21 18:24:48
	 */
	public void start() {
		
		this.init();
		
		this.coreServer.start().addListener(f -> {
			if (f.isSuccess()) {
				this.registryClient.start();
			}
		});
		
	}

}
