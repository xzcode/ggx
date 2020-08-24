package com.ggx.server.loadbalancer;

import java.util.List;

import com.ggx.core.common.utils.GGXIdUtil;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.client.registry.RegistryInfo;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.xzcode.ggserver.core.server.GGXCoreServer;
import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;

public class GGXLoadbalancerServer {
	
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

}
