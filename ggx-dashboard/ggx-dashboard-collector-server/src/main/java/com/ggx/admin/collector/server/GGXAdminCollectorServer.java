package com.ggx.admin.collector.server;

import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.admin.collector.server.events.ConnActiveEventListener;
import com.ggx.admin.collector.server.events.ConnCloseEventListener;
import com.ggx.admin.collector.server.filter.AuthFilter;
import com.ggx.admin.collector.server.handler.AuthReqHandler;
import com.ggx.admin.collector.server.handler.ServiceDataReqHandler;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.message.receive.support.ReceiveMessageSupport;
import com.xzcode.ggserver.core.server.GGXCoreServer;
import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;
import com.xzcode.ggserver.core.server.impl.GGXDefaultCoreServer;


public class GGXAdminCollectorServer  implements ReceiveMessageSupport, EventSupport{
	
	private GGXAdminCollectorServerConfig config;
	
	
	
	public GGXAdminCollectorServer(GGXAdminCollectorServerConfig config) {
		super();
		this.config = config;
		init();
	}

	public void init() {
		
		GGXCoreServerConfig ggconfig = new GGXCoreServerConfig();
		ggconfig.setPingPongEnabled(true);
		ggconfig.setPrintPingPongInfo(config.isPrintPingPongInfo());
		ggconfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggconfig.setPort(config.getPort());
		ggconfig.setBossGroupThreadFactory(new GGThreadFactory("admin-collector-server-boss-", false));
		ggconfig.setWorkerGroupThreadFactory(new GGThreadFactory("admin-collector-server-worker-", false));
		ggconfig.init();
		GGXCoreServer ggserver = new GGXDefaultCoreServer(ggconfig);
		
		ggserver.addEventListener(GGXCoreEvents.Connection.OPENED, new ConnActiveEventListener(config));
		ggserver.addEventListener(GGXCoreEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		
		ggserver.addFilter(new AuthFilter());
		
		ggserver.onMessage(new AuthReqHandler(config));
		ggserver.onMessage(new ServiceDataReqHandler(config));
		
		
		
		config.setServer(ggserver);
		
		
	}
	
	public void start() {
		config.getServer().start();
	}
	
	public void setConfig(GGXAdminCollectorServerConfig config) {
		this.config = config;
	}
	
	public GGXAdminCollectorServerConfig getConfig() {
		return config;
	}

	@Override
	public EventManager getEventManager() {
		return this.config.getServer().getEventManager();
	}

	@Override
	public ReceiveMessageManager getReceiveMessageManager() {
		return this.config.getServer().getReceiveMessageManager();
	}
	
}
