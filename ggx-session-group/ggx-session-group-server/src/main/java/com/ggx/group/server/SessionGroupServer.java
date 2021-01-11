package com.ggx.group.server;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.core.server.impl.GGXDefaultCoreServer;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.common.session.SessionGroupSessionFactory;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.group.server.events.ConnActiveEventListener;
import com.ggx.group.server.events.ConnCloseEventListener;
import com.ggx.group.server.handler.AuthReqHandler;
import com.ggx.group.server.handler.DataTransferReqHandler;
import com.ggx.group.server.handler.SessionGroupRegisterReqHandler;
import com.ggx.util.thread.GGXThreadFactory;

/**
 * 会话组服务器启动类
 *
 * @author zai
 * 2020-04-08 15:21:21
 */
public class SessionGroupServer {
	
	private SessionGroupServerConfig config;
	
	
	
	public SessionGroupServer(SessionGroupServerConfig config) {
		super();
		this.config = config;
		init();
	}

	public void init() {
		
		GGXThreadFactory bossThreadFactory = new GGXThreadFactory("gg-group-boss-", false);
		if (this.config.getWorkThreadFactory() == null) {
			this.config.setWorkThreadFactory(new GGXThreadFactory("gg-group-worker-", false));
		}
		
		GGXCoreServerConfig sessionServerConfig = new GGXCoreServerConfig();
		sessionServerConfig.setGgxComponent(true);
		sessionServerConfig.setPingPongEnabled(true);
		sessionServerConfig.setProtocolType(ProtocolTypeConstants.TCP);
		sessionServerConfig.setPort(this.config.getPort());
		sessionServerConfig.setSessionFactory(new SessionGroupSessionFactory(sessionServerConfig));
		sessionServerConfig.setBossGroupThreadFactory(bossThreadFactory);
		sessionServerConfig.setWorkerGroupThreadFactory(this.config.getWorkThreadFactory());
		sessionServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionServerConfig.setPortChangeStrategy(this.config.getPortChangeStrategy());
		sessionServerConfig.setChangeAndRebootIfPortInUse(this.config.isChangeAndRebootIfPortInUse());
		sessionServerConfig.setBootWithRandomPort(this.config.isBootWithRandomPort());
		
		
		sessionServerConfig.init();
		
		GGSessionGroupManager sessionGroupManager = new GGSessionGroupManager(sessionServerConfig);
		this.config.setSessionGroupManager(sessionGroupManager);
		
		GGXCoreServer sessionServer = new GGXDefaultCoreServer(sessionServerConfig);
		sessionServer.addEventListener(GGXCoreEvents.Connection.OPENED, new ConnActiveEventListener(config));
		sessionServer.addEventListener(GGXCoreEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		sessionServer.registerMessageController(new AuthReqHandler(config));
		sessionServer.registerMessageController(new SessionGroupRegisterReqHandler(config));
		sessionServer.registerMessageController(new DataTransferReqHandler(config));
		
		this.config.setSessionServer(sessionServer);
		
		
		
		
		GGXCoreServerConfig serviceServerConfig = new GGXCoreServerConfig();
		serviceServerConfig.setGgxComponent(true);
		serviceServerConfig.setActionIdPrefix(config.getServiceActionIdPrefix());
		serviceServerConfig.setBossGroup(sessionServerConfig.getBossGroup());
		serviceServerConfig.setWorkerGroup(sessionServerConfig.getWorkerGroup());
		serviceServerConfig.init();
		
		GGXCoreServer serviceServer = new GGXDefaultCoreServer(serviceServerConfig);
		this.config.setServiceServer(serviceServer);
		
	}
	
	public GGXFuture<?> shutdown() {
		return this.config.getSessionServer().shutdown();
	}
	
	public GGXFuture<?> start() {
		return this.config.getSessionServer().start();
	}
	
	public void setConfig(SessionGroupServerConfig config) {
		this.config = config;
	}
	
	public SessionGroupServerConfig getConfig() {
		return config;
	}
	
}
