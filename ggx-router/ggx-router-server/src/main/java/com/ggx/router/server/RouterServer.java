package com.ggx.router.server;

import java.nio.charset.Charset;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.FilterSupport;
import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.filter.SendMessageFilter;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.handler.serializer.ISerializer;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.receive.action.MessageDataHandler;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.message.receive.support.ReceiveMessageSupport;
import com.ggx.core.common.message.send.support.SendMessageSupport;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.common.constant.RouterConstant;
import com.ggx.router.common.constant.RouterServiceCustomDataKeys;
import com.ggx.router.common.constant.RouterSessionDisconnectTransferType;
import com.ggx.router.common.message.req.RouterSessionDisconnectTransferReq;
import com.ggx.router.common.message.resp.RouterRedirectMessageToOtherRouterServicesResp;
import com.ggx.router.common.message.resp.RouterSessionDisconnectTransferResp;
import com.ggx.router.server.config.RouterServerConfig;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 路由服务器对象
 * 
 * @author zai 2019-12-05 10:34:03
 */
public class RouterServer implements SendMessageSupport, ReceiveMessageSupport, FilterSupport, ExecutorSupport, EventSupport {

	private RouterServerConfig config;
	
	
	protected GGServer serviceServer;


	public RouterServer(RouterServerConfig config) {

		this.config = config;

		init();
	}

	public void init() {
		
		if (this.config.getSharedEventLoopGroup() == null) {
			this.config.setSharedEventLoopGroup(new NioEventLoopGroup(this.config.getWorkThreadSize(), new GGThreadFactory("gg-router-serv-", false)));
		}

		SessionGroupServerConfig sessionServerConfig = new SessionGroupServerConfig();
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
		
		
		this.serviceServer = sessionServerConfig.getServiceServer();
		
		//添加发送过滤器
		this.serviceServer.addFilter(new SendMessageFilter() {
			
			@Override
			public boolean doFilter(MessageData<?> data) {
				GGSession session = data.getSession();
				String action = data.getAction();
				if (action.startsWith(RouterConstant.ACTION_ID_PREFIX)) {
					session.send(session.makePack(data));
					return false;
				}
				return true;
			}
		});
		
		//添加接收消息过滤器
		this.serviceServer.addFilter(new ReceiveMessageFilter() {
			
			@Override
			public boolean doFilter(MessageData<?> data) {
				String action = data.getAction();
				if (action.startsWith(RouterConstant.ACTION_ID_PREFIX)) {
					serviceServer.getReceiveMessageManager().handle(data);
					return false;
				}
				return true;
			}
		});
		
		//添加连接断开监听
		this.serviceServer.addEventListener(GGEvents.Connection.CLOSED, new EventListener<Void>() {
			@Override
			public void onEvent(EventData<Void> eventData) {
				GGSession session = eventData.getSession();
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
		this.serviceServer.onMessage(RouterSessionDisconnectTransferReq.ACTION_ID, new MessageDataHandler<RouterSessionDisconnectTransferReq>() {

			@Override
			public void handle(MessageData<RouterSessionDisconnectTransferReq> messageData) {
				RouterSessionDisconnectTransferReq req = messageData.getMessage();
				String tranferSessionId = req.getTranferSessionId();
				SessionManager sessionManager = serviceServer.getSessionManager();
				GGSession session = sessionManager.getSession(tranferSessionId);
				if (session != null) {
					session.addAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, RouterSessionDisconnectTransferType.REQ);
					session.disconnect();
				}
			}
			
		});

	}

	public GGServer getServiceServer() {
		return this.serviceServer;
	}

	public RouterServerConfig getConfig() {
		return config;
	}

	public GGFuture start() {
		GGFuture startFuture = this.config.getSessionGroupServer().start();
		startFuture.addListener(f -> {
			if (f.isSuccess()) {
				this.config.setPort(this.config.getSessionGroupServer().getConfig().getSessionServer().getConfig().getPort());
				RegistryClient registryClient = config.getRegistryClient();
				if (registryClient != null) {
					if (config.getRouterGroupId() != null) {
						registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_GROUP_ID,config.getRouterGroupId());
					}
					if (config.getActionIdPrefix() != null) {
						registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_ACTION_ID_PREFIX,config.getActionIdPrefix());
					}
					registryClient.getConfig().addCustomData(RouterServiceCustomDataKeys.ROUTER_SERVICE_PORT, String.valueOf(this.config.getPort()));
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
	public void redirectMessageToOtherRouterServices(GGSession redirectingSession, String redirectServiceId, Message redirectingMessage) {
		
		RouterRedirectMessageToOtherRouterServicesResp resp = new RouterRedirectMessageToOtherRouterServicesResp();
		
		Pack pack = makePack(new MessageData<>(redirectingMessage.getActionId(), redirectingMessage));
		
		resp.setServiceId(redirectServiceId);
		resp.setSessionId(redirectingSession.getSessonId());
		resp.setAction(pack.getAction());
		resp.setMessage(pack.getMessage());
		resp.setSerializeType(pack.getSerializeType());
		
		redirectingSession.send(resp);
		
		
	}

	@Override
	public Charset getCharset() {
		return this.serviceServer.getCharset();
	}

	private GGServerConfig getServiceServerConfig() {
		return this.serviceServer.getConfig();
	}

	@Override
	public ISerializer getSerializer() {
		return this.getServiceServerConfig().getSerializer();
	}

	@Override
	public EventManager getEventManager() {
		return this.getServiceServerConfig().getEventManager();
	}

	@Override
	public TaskExecutor getTaskExecutor() {
		return this.getServiceServerConfig().getTaskExecutor();
	}

	@Override
	public ReceiveMessageManager getReceiveMessageManager() {
		return this.getServiceServerConfig().getReceiveMessageManager();
	}

	@Override
	public SessionManager getSessionManager() {
		return this.getServiceServerConfig().getSessionManager();
	}

	@Override
	public FilterManager getFilterManager() {
		return this.getServiceServerConfig().getFilterManager();
	}

}
