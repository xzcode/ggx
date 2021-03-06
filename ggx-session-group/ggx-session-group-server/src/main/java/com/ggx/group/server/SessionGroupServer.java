package com.ggx.group.server;

import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.future.GGFuture;
import com.ggx.group.common.constant.GGSesssionGroupConstant;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.common.message.req.AuthReq;
import com.ggx.group.common.message.req.DataTransferReq;
import com.ggx.group.common.message.req.SessionGroupRegisterReq;
import com.ggx.group.common.session.SessionGroupSessionFactory;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.group.server.events.ConnActiveEventListener;
import com.ggx.group.server.events.ConnCloseEventListener;
import com.ggx.group.server.handler.AuthReqHandler;
import com.ggx.group.server.handler.DataTransferReqHandler;
import com.ggx.group.server.handler.SessionGroupRegisterReqHandler;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;
import com.xzcode.ggserver.core.server.impl.GGDefaultServer;

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
		
		GGThreadFactory bossThreadFactory = new GGThreadFactory("gg-group-boss-", false);
		if (this.config.getWorkThreadFactory() == null) {
			this.config.setWorkThreadFactory(new GGThreadFactory("gg-group-worker-", false));
		}
		
		GGServerConfig sessionServerConfig = new GGServerConfig();
		sessionServerConfig.setPingPongEnabled(true);
		sessionServerConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionServerConfig.setProtocolType(ProtocolTypeConstants.TCP);
		sessionServerConfig.setPort(this.config.getPort());
		sessionServerConfig.setSessionFactory(new SessionGroupSessionFactory(sessionServerConfig));
		sessionServerConfig.setBossGroupThreadFactory(bossThreadFactory);
		sessionServerConfig.setWorkerGroupThreadFactory(this.config.getWorkThreadFactory());
		sessionServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionServerConfig.setPortChangeStrategy(this.config.getPortChangeStrategy());
		sessionServerConfig.setChangeAndRebootIfPortInUse(this.config.isChangeAndRebootIfPortInUse());
		sessionServerConfig.setBootWithRandomPort(this.config.isBootWithRandomPort());
		
		if (!this.config.isPrintSessionGroupPackLog()) {
			sessionServerConfig.getPackLogger().addPackLogFilter(pack -> {
				String actionString = pack.getActionString();
				return !(actionString.startsWith(GGSesssionGroupConstant.ACTION_ID_PREFIX));
			});
		}
		sessionServerConfig.init();
		
		GGSessionGroupManager sessionGroupManager = new GGSessionGroupManager(sessionServerConfig);
		this.config.setSessionGroupManager(sessionGroupManager);
		
		GGServer sessionServer = new GGDefaultServer(sessionServerConfig);
		sessionServer.addEventListener(GGEvents.Connection.OPENED, new ConnActiveEventListener(config));
		sessionServer.addEventListener(GGEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		sessionServer.onMessage(AuthReq.ACTION, new AuthReqHandler(config));
		sessionServer.onMessage(SessionGroupRegisterReq.ACTION_ID, new SessionGroupRegisterReqHandler(config));
		sessionServer.onMessage(DataTransferReq.ACTION, new DataTransferReqHandler(config));
		
		this.config.setSessionServer(sessionServer);
		
		
		
		
		GGServerConfig serviceServerConfig = new GGServerConfig();
		serviceServerConfig.setBossGroup(sessionServerConfig.getBossGroup());
		serviceServerConfig.setWorkerGroup(sessionServerConfig.getWorkerGroup());
		serviceServerConfig.init();
		
		GGServer serviceServer = new GGDefaultServer(serviceServerConfig);
		this.config.setServiceServer(serviceServer);
		
	}
	
	public GGFuture start() {
		return this.config.getSessionServer().start();
	}
	
	public void setConfig(SessionGroupServerConfig config) {
		this.config = config;
	}
	
	public SessionGroupServerConfig getConfig() {
		return config;
	}
	
}
