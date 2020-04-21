package com.xzcode.ggcloud.discovery.server;

import com.xzcode.ggcloud.discovery.common.message.req.DiscoveryServiceListReq;
import com.xzcode.ggcloud.discovery.common.message.req.DiscoveryServiceRegisterReq;
import com.xzcode.ggcloud.discovery.common.message.req.DiscoveryServiceUpdateReq;
import com.xzcode.ggcloud.discovery.server.config.DiscoveryServerConfig;
import com.xzcode.ggcloud.discovery.server.events.ConnActiveEventListener;
import com.xzcode.ggcloud.discovery.server.events.ConnCloseEventListener;
import com.xzcode.ggcloud.discovery.server.handler.RegisterReqHandler;
import com.xzcode.ggcloud.discovery.server.handler.ServiceListReqHandler;
import com.xzcode.ggcloud.discovery.server.handler.ServiceUpdateReqHandler;
import com.xzcode.ggserver.core.common.constant.ProtocolTypeConstants;
import com.xzcode.ggserver.core.common.event.GGEvents;
import com.xzcode.ggserver.core.common.executor.thread.GGThreadFactory;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;
import com.xzcode.ggserver.core.server.impl.GGDefaultServer;

public class DiscoveryServer {
	
	private DiscoveryServerConfig config;
	
	
	
	public DiscoveryServer(DiscoveryServerConfig config) {
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
		
		ggServer.onMessage(DiscoveryServiceRegisterReq.ACTION, new RegisterReqHandler(config));
		ggServer.onMessage(DiscoveryServiceListReq.ACTION, new ServiceListReqHandler(config));
		ggServer.onMessage(DiscoveryServiceUpdateReq.ACTION, new ServiceUpdateReqHandler(config));
		
		ggServer.start();
		
		config.setServer(ggServer);
		
	}
	
	public void setConfig(DiscoveryServerConfig config) {
		this.config = config;
	}
	
	public DiscoveryServerConfig getConfig() {
		return config;
	}
	
}
