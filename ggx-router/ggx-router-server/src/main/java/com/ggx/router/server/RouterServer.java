package com.ggx.router.server;

import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.config.GGXCoreSupport;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.filter.SendMessageFilter;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.receive.controller.MessageController;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.common.constant.RouterConstant;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;
import com.ggx.router.common.constant.RouterSessionDisconnectTransferType;
import com.ggx.router.common.message.req.RouteMessageReq;
import com.ggx.router.common.message.req.RouterSessionDisconnectTransferReq;
import com.ggx.router.common.message.resp.RouterRedirectMessageToOtherRouterServicesResp;
import com.ggx.router.common.message.resp.RouterSessionDisconnectTransferResp;
import com.ggx.router.server.config.RouterServerConfig;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 路由服务器对象
 * 
 * @author zai 2019-12-05 10:34:03
 */
public class RouterServer implements GGXCoreSupport {

	private RouterServerConfig config;
	
	
	protected GGXCoreServer sessionServiceServer;


	public RouterServer(RouterServerConfig config) {

		this.config = config;

		init();
	}

	public void init() {
		
		if (this.config.getSharedEventLoopGroup() == null) {
			this.config.setSharedEventLoopGroup(new NioEventLoopGroup(this.config.getWorkThreadSize(), new GGXThreadFactory("gg-router-serv-", false)));
		}

		SessionGroupServerConfig sessionServerConfig = new SessionGroupServerConfig();
		sessionServerConfig.setServiceActionIdPrefix(config.getActionIdPrefix());
		sessionServerConfig.setAuthToken(this.config.getAuthToken());
		sessionServerConfig.setEnableServiceServer(true);
		sessionServerConfig.setPort(this.config.getPort());
		sessionServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionServerConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		//sessionGroupServerConfig.setWorkThreadFactory(new GGThreadFactory("gg-router-serv-", false));
		sessionServerConfig.setPortChangeStrategy(this.config.getPortChangeStrategy());
		sessionServerConfig.setChangeAndRebootIfPortInUse(this.config.isChangeAndRebootIfPortInUse());
		sessionServerConfig.setBootWithRandomPort(this.config.isBootWithRandomPort());
		
		if (this.config.getSharedEventLoopGroup() != null) {
			sessionServerConfig.setWorkEventLoopGroup(this.config.getSharedEventLoopGroup());

		}

		SessionGroupServer sessionServer = new SessionGroupServer(sessionServerConfig);
		this.config.setSessionGroupServer(sessionServer);
		
		
		this.sessionServiceServer = sessionServerConfig.getServiceServer();
		this.sessionServiceServer.getConfig().setIgnoreActionIdPrefixes(config.getIgnoreActionIdPrefixes());
		
		/*
		 * //添加发送过滤器 this.serviceServer.addFilter(new SendMessageFilter() {
		 * 
		 * @Override public boolean doFilter(MessageData<?> data) { GGXSession session =
		 * data.getSession(); String action = data.getAction(); if
		 * (action.startsWith(RouterConstant.ACTION_ID_PREFIX)) {
		 * session.send(session.makePack(data)); return false; } return true; } });
		 * 
		 * //添加接收消息过滤器 this.serviceServer.addFilter(new ReceiveMessageFilter() {
		 * 
		 * @Override public boolean doFilter(MessageData<?> data) { String action =
		 * data.getAction(); if (action.startsWith(RouterConstant.ACTION_ID_PREFIX)) {
		 * serviceServer.getMessageControllerManager().invoke(data); return false; }
		 * return true; } });
		 */
		//添加连接断开监听
		this.sessionServiceServer.addEventListener(GGXCoreEvents.Connection.CLOSED, new EventListener<Void>() {
			@Override
			public void onEvent(EventData<Void> eventData) {
				GGXSession session = eventData.getSession();
				if (session != null) {
					Integer tranType = session.getAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, Integer.class);
					if (tranType  != null && RouterSessionDisconnectTransferType.REQ == tranType) {
						return;
					}
					if (RouterServer.this.config.isSessionDisconnectTransferResponseEnabled()) {
						session.send(new RouterSessionDisconnectTransferResp(session.getSessonId()));
					}
				}
			}
		} );
		
		//监听session断开传递
		this.sessionServiceServer.register(new MessageController(){

			@GGXAction
			public void disconnectTransferReq(RouterSessionDisconnectTransferReq req) {
				String tranferSessionId = req.getTranferSessionId();
				SessionManager sessionManager = sessionServiceServer.getSessionManager();
				GGXSession session = sessionManager.getSession(tranferSessionId);
				if (session != null) {
					session.addAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, RouterSessionDisconnectTransferType.REQ);
					session.disconnect();
				}
			}
			@GGXAction
			public void routeMessageReq(RouteMessageReq req) {
				String tranferSessionId = req.getTranferSessionId();
				SessionManager sessionManager = sessionServiceServer.getSessionManager();
				GGXSession session = sessionManager.getSession(tranferSessionId);
				if (session != null) {
					session.addAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, RouterSessionDisconnectTransferType.REQ);
					session.disconnect();
				}
			}
			
		});
		
		

	}

	public GGXCoreServer getSessionServiceServer() {
		return this.sessionServiceServer;
	}

	public RouterServerConfig getConfig() {
		return config;
	}

	public GGXFuture start() {
		GGXFuture startFuture = this.config.getSessionGroupServer().start();
		startFuture.addListener(f -> {
			if (f.isSuccess()) {
				this.config.setPort(this.config.getSessionGroupServer().getConfig().getSessionServer().getConfig().getPort());
				RegistryClient registryClient = config.getRegistryClient();
				if (registryClient != null) {
					if (config.getRouterGroupId() != null) {
						registryClient.addCustomData(RouterServiceCustomDataKeys.ROUTER_GROUP_ID,config.getRouterGroupId());
					}
					if (config.getActionIdPrefix() != null) {
						registryClient.addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX,config.getActionIdPrefix());
					}
					registryClient.getConfig().setPort(this.config.getPort());
					registryClient.addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_PORT, String.valueOf(this.config.getPort()));
				}
				
			}
		});
		
		return startFuture;
	}
	
	/**
	 * 重定向消息到其他路由服务
	 *
	 * @param redirectingSession
	 * @param redirectingMessage
	 * @author zai
	 * 2020-06-01 14:55:48
	 */
	public void redirectMessageToOtherRouterServices(GGXSession redirectingSession, String redirectServiceId, Message redirectingMessage) {
		
		RouterRedirectMessageToOtherRouterServicesResp resp = new RouterRedirectMessageToOtherRouterServicesResp();
		
		Pack pack = makePack(new MessageData<>(redirectingSession.getActionIdCacheManager().get(redirectingMessage.getClass()), redirectingMessage));
		// 序列化后发送过滤器
		if (!redirectingSession.getFilterManager().doAfterSerializeFilters(pack)) {
			return;
		}
		resp.setServiceId(redirectServiceId);
		resp.setSessionId(redirectingSession.getSessonId());
		resp.setAction(pack.getAction());
		resp.setMessage(pack.getMessage());
		resp.setSerializeType(pack.getSerializeType());
		
		redirectingSession.send(resp);
		
		
	}
	
	
	public GGXFuture shutdown() {
		return this.config.getSessionGroupServer().shutdown();
	}


	@Override
	public GGXCore getGGXCore() {
		return this.getSessionServiceServer();
	}


}
