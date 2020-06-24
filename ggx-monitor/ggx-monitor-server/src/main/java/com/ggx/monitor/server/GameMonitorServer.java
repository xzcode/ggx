package com.ggx.monitor.server;

import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.message.receive.support.ReceiveMessageSupport;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.monitor.common.message.req.AuthReq;
import com.ggx.monitor.server.config.GameMonitorServerConfig;
import com.ggx.monitor.server.constant.GameMonitorServerSessionKeys;
import com.ggx.monitor.server.events.ConnActiveEventListener;
import com.ggx.monitor.server.events.ConnCloseEventListener;
import com.ggx.monitor.server.handler.AuthReqHandler;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;
import com.xzcode.ggserver.core.server.impl.GGDefaultServer;


public class GameMonitorServer  implements ReceiveMessageSupport, EventSupport{
	
	private GameMonitorServerConfig config;
	
	
	
	public GameMonitorServer(GameMonitorServerConfig config) {
		super();
		this.config = config;
		init();
	}

	public void init() {
		
		GGServerConfig ggconfig = new GGServerConfig();
		ggconfig.setPingPongEnabled(true);
		ggconfig.setPrintPingPongInfo(config.isPrintPingPongInfo());
		ggconfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggconfig.setPort(config.getPort());
		ggconfig.setBossGroupThreadFactory(new GGThreadFactory("monitor-boss-", false));
		ggconfig.setWorkerGroupThreadFactory(new GGThreadFactory("monitor-worker-", false));
		ggconfig.init();
		GGServer ggserver = new GGDefaultServer(ggconfig);
		
		ggserver.addEventListener(GGEvents.Connection.OPENED, new ConnActiveEventListener(config));
		
		ggserver.addEventListener(GGEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		
		//添加认证过滤器
		ggserver.addFilter(new ReceiveMessageFilter() {
			
			@Override
			public boolean doFilter(MessageData<?> messageData) {
					if (messageData.getAction().equals(AuthReq.ACTION_ID)) {
						return true;
					}
					
					GGSession session = messageData.getSession();
					Boolean auth = session.getAttribute(GameMonitorServerSessionKeys.IS_AUTHED, Boolean.class);
					if (auth != null && auth) {
						return true;
					}
					GGLoggerUtil.getLogger(this).warn("Session Not Authed!");
					return false;
			}
		});
		
		//注册认证处理器
		ggserver.onMessage(AuthReq.ACTION_ID, new AuthReqHandler(config));
		
		config.setServer(ggserver);
		
		
	}
	
	public void start() {
		config.getServer().start();
	}
	
	public void setConfig(GameMonitorServerConfig config) {
		this.config = config;
	}
	
	public GameMonitorServerConfig getConfig() {
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
