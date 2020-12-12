package com.ggx.router.server;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.config.GGXCoreSupport;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.receive.controller.MessageController;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.core.server.impl.GGXDefaultCoreServer;
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
import com.ggx.router.server.session.RouterServerSession;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 路由服务器对象
 * 
 * @author zai 2019-12-05 10:34:03
 */
public class RouterServer implements GGXCoreSupport {

	private RouterServerConfig config;
	
	
	protected GGXCoreServer sessionServiceServer;
	protected GGXCoreServer hostServer;


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
		sessionServerConfig.setPort(this.config.getPort());
		sessionServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionServerConfig.setPortChangeStrategy(this.config.getPortChangeStrategy());
		sessionServerConfig.setChangeAndRebootIfPortInUse(this.config.isChangeAndRebootIfPortInUse());
		sessionServerConfig.setBootWithRandomPort(this.config.isBootWithRandomPort());
		
		if (this.config.getSharedEventLoopGroup() != null) {
			sessionServerConfig.setWorkEventLoopGroup(this.config.getSharedEventLoopGroup());

		}

		SessionGroupServer sessionServer = new SessionGroupServer(sessionServerConfig);
		this.config.setSessionGroupServer(sessionServer);
		
		
		
		
		GGXCoreServerConfig hostServerConfig = new GGXCoreServerConfig();
		hostServerConfig.setWorkerGroup(this.config.getSharedEventLoopGroup());
		hostServerConfig.init();
		
		this.hostServer = new GGXDefaultCoreServer(hostServerConfig);
		
		
		
		this.sessionServiceServer = sessionServerConfig.getServiceServer();
		this.sessionServiceServer.getConfig().setIgnoreActionIdPrefixes(config.getIgnoreActionIdPrefixes());
		
		
		//监听session断开传递
		this.sessionServiceServer.registerMessageController(new MessageController(){

			@GGXAction
			public void disconnectTransferReq(RouterSessionDisconnectTransferReq req) {
				String tranferSessionId = req.getTranferSessionId();
				SessionManager hostSessionManager = hostServer.getSessionManager();
				GGXSession hostSession = hostSessionManager.getSession(tranferSessionId);
				if (hostSession != null) {
					hostSession.addAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, RouterSessionDisconnectTransferType.REQ);
					hostSession.disconnect();
				}
			}
			
			@GGXAction
			public void routeMessageReq(RouteMessageReq req, GGXSession session) {
				String tranferSessionId = req.getTranferSessionId();
				byte[] action = req.getAction();
				String serializeType = req.getSerializeType();
				byte[] message = req.getMessage();
				
				SessionManager hostSessionManager = hostServer.getSessionManager();
				GGXSession hostSession = hostSessionManager.getSession(tranferSessionId);
				GGXCoreServerConfig hostServerConfig = hostServer.getConfig();
				if (hostSession == null) {
					hostSession = new RouterServerSession(tranferSessionId, session, sessionServiceServer.getSessionManager(), hostServerConfig);
					GGXSession addSessionIfAbsent = hostSessionManager.addSessionIfAbsent(hostSession);
					if (addSessionIfAbsent != null) {
						hostSession = addSessionIfAbsent;
					}
				}
				
				Pack pack = new Pack(hostSession, action, message);
				pack.setSerializeType(serializeType);
				
				hostServerConfig.getReceiveMessageManager().receive(pack);
			}
			
		});
		
		
		
		
		//添加连接断开监听
		this.hostServer.addEventListener(GGXCoreEvents.Connection.CLOSED, new EventListener<Void>() {
			@Override
			public void onEvent(EventData<Void> eventData) {
				GGXSession hostSession = eventData.getSession();
				if (hostSession != null) {
					Integer tranType = hostSession.getAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, Integer.class);
					if (tranType  != null && RouterSessionDisconnectTransferType.REQ == tranType) {
						return;
					}
					if (config.isSessionDisconnectTransferResponseEnabled()) {
						sessionServiceServer.getSessionManager().getRandomSession().send(new RouterSessionDisconnectTransferResp(hostSession.getSessionId()));
					}
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

	public GGXFuture<?> start() {
		GGXFuture<?> startFuture = this.config.getSessionGroupServer().start();
		startFuture.addListener(f -> {
			if (f.isSuccess()) {
				this.config.setPort(this.config.getSessionGroupServer().getConfig().getSessionServer().getConfig().getPort());
				RegistryClient registryClient = config.getRegistryClient();
				if (registryClient != null) {
					if (config.getRouterGroupId() != null) {
						registryClient.addCustomData(RouterServiceCustomDataKeys.ROUTER_GROUP_ID,config.getRouterGroupId(), false);
					}
					if (config.getActionIdPrefix() != null) {
						registryClient.addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX,config.getActionIdPrefix(), false);
					}
					registryClient.getConfig().setPort(this.config.getPort());
					registryClient.addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_PORT, String.valueOf(this.config.getPort()), false);
					registryClient.updateService();
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
		
		Pack pack = makePack(new MessageData(redirectingSession.getActionIdCacheManager().get(redirectingMessage.getClass()), redirectingMessage));
		resp.setServiceId(redirectServiceId);
		resp.setTranferSessionId(redirectingSession.getSessionId());
		resp.setAction(pack.getAction());
		resp.setMessage(pack.getMessage());
		resp.setSerializeType(pack.getSerializeType());
		
		redirectingSession.send(resp);
		
		
	}
	
	
	public GGXFuture<?> shutdown() {
		return this.config.getSessionGroupServer().shutdown();
	}


	@Override
	public GGXCore getGGXCore() {
		return this.getHostServer();
	}
	
	public GGXCoreServer getHostServer() {
		return hostServer;
	}

}
