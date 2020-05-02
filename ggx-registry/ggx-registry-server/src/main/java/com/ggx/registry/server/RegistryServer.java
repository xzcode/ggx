package com.ggx.registry.server;

import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.registry.common.message.req.RegistryServiceListReq;
import com.ggx.registry.common.message.req.RegistryServiceRegisterReq;
import com.ggx.registry.common.message.req.RegistryServiceUpdateReq;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.registry.server.events.ConnActiveEventListener;
import com.ggx.registry.server.events.ConnCloseEventListener;
import com.ggx.registry.server.handler.RegisterReqHandler;
import com.ggx.registry.server.handler.ServiceListReqHandler;
import com.ggx.registry.server.handler.ServiceUpdateReqHandler;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;
import com.xzcode.ggserver.core.server.impl.GGDefaultServer;

public class RegistryServer {
	
	private RegistryServerConfig config;
	
	
	
	public RegistryServer(RegistryServerConfig config) {
		super();
		this.config = config;
	}

	public void start() {
		
		GGServerConfig ggConfig = new GGServerConfig();
		ggConfig.setPingPongEnabled(true);
		ggConfig.setPrintPingPongInfo(config.isPrintPingPongInfo());
		ggConfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggConfig.setPort(config.getPort());
		ggConfig.setBossGroupThreadFactory(new GGThreadFactory("discovery-boss-", false));
		ggConfig.setWorkerGroupThreadFactory(new GGThreadFactory("discovery-worker-", false));
		ggConfig.init();
		GGServer ggServer = new GGDefaultServer(ggConfig);
		
		ggServer.addEventListener(GGEvents.Connection.OPENED, new ConnActiveEventListener(config));
		
		ggServer.addEventListener(GGEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		
		ggServer.onMessage(RegistryServiceRegisterReq.ACTION_ID, new RegisterReqHandler(config));
		ggServer.onMessage(RegistryServiceListReq.ACTION_ID, new ServiceListReqHandler(config));
		ggServer.onMessage(RegistryServiceUpdateReq.ACTION_ID, new ServiceUpdateReqHandler(config));
		
		ggServer.start();
		
		config.setServer(ggServer);
		
	}
	
	public void setConfig(RegistryServerConfig config) {
		this.config = config;
	}
	
	public RegistryServerConfig getConfig() {
		return config;
	}
	
}
