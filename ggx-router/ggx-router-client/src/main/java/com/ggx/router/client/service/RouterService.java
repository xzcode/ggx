package com.ggx.router.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.filter.ReceivePackFilter;
import com.ggx.core.common.filter.chain.FilterChain;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.controller.MessageController;
import com.ggx.core.common.message.receive.controller.MessageControllerManager;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.event.RouterClientEvents;
import com.ggx.router.client.service.listener.RouterServiceShutdownListener;
import com.ggx.router.client.service.loadblancer.RouterServiceLoadbalancer;
import com.ggx.router.client.service.manager.RouterServiceManager;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;
import com.ggx.router.common.constant.RouterConstant;
import com.ggx.router.common.constant.RouterSessionDisconnectTransferType;
import com.ggx.router.common.message.req.RouteMessageReq;
import com.ggx.router.common.message.req.RouterSessionDisconnectTransferReq;
import com.ggx.router.common.message.resp.RouterRedirectMessageToOtherRouterServicesResp;
import com.ggx.router.common.message.resp.RouterSessionDisconnectTransferResp;
import com.ggx.session.group.client.SessionGroupClient;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 路由服务
 * 
 * @author zai
 * 2019-11-07 16:52:05
 */
public class RouterService {
	
	protected RouterClientConfig config;
	
	protected String serviceId;
	
	protected String serviceGroupId;
	
	protected String actionIdPrefix;
	
	protected String serviceName;
	
	protected String host;
	
	protected int port;
	
	protected TaskExecutor executor;
	
	//自定义业务客户端
	protected GGXCoreClient customServiceClient;
	
	//业务客户端
	protected GGXCoreClient serviceClient;
	
	//绑定的连接客户端
	protected SessionGroupClient sessionGroupClient;
	
	
	
	/**
	 * 是否已准备接收数据
	 */
	protected AtomicInteger avaliableConnections = new AtomicInteger(0);
	
	/**
	 * 额外数据
	 */
	protected Map<String, String> customData = new ConcurrentHashMap<>();
	
	/**
	 * 服务关闭监听器
	 */
	protected List<RouterServiceShutdownListener> shutdownListeners = new ArrayList<>();
	
	
	/**
	 * 是否已关闭
	 */
	protected boolean shutdown;
	
	/**
	 * 负载量
	 */
	protected AtomicInteger load = new AtomicInteger(0);
	
	

	public RouterService(RouterClientConfig config, String serviceId) {
		this.config = config;
		this.serviceId = serviceId;
	}
	
	/**
	 * 初始化
	 * 
	 * @author zai
	 * 2019-11-07 15:50:25
	 */
	public void init() {
		
		SessionGroupClientConfig sessionGroupClientConfig = new SessionGroupClientConfig();
		sessionGroupClientConfig.setEnableServiceClient(true);
		sessionGroupClientConfig.setWorkEventLoopGroup(this.config.getSharedEventLoopGroup());
		sessionGroupClientConfig.setAuthToken(this.config.getAuthToken());
		sessionGroupClientConfig.setConnectionSize(this.config.getConnectionSize());
		sessionGroupClientConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionGroupClientConfig.setServerHost(this.host);
		sessionGroupClientConfig.setServerPort(this.port);
		
		
		SessionGroupClient sessionGroupClient = new SessionGroupClient(sessionGroupClientConfig);
		
		this.sessionGroupClient = sessionGroupClient;
		
		
		//自定义业务客户端
		GGXCoreClientConfig customServiceClientConfig = new GGXCoreClientConfig();
		customServiceClientConfig.setWorkerGroup(this.config.getSharedEventLoopGroup());
		customServiceClientConfig.init();
		this.customServiceClient = new GGXCoreClient(customServiceClientConfig);
		
		
		
		//包日志输出控制
		if (!this.config.isPrintRouterInfo()) {
			sessionGroupClientConfig.getServiceClient().getConfig().getPackLogger().addPackLogFilter(pack -> {
				return false;
			});
		}
		
		this.serviceClient = sessionGroupClientConfig.getServiceClient();
		
		MessageControllerManager controllerManager = serviceClient.getMessageControllerManager();
		
		this.executor = this.serviceClient.getTaskExecutor().nextEvecutor();
		
		this.serviceClient.addFilter(new ReceivePackFilter() {
			
			@Override
			public void doFilter(Pack pack, FilterChain<Pack> filterChain) {
				String actionString = pack.getActionString();
				if (controllerManager.getMethodInfo(actionString) != null) {
					filterChain.doFilter(pack);
					return;
				}
				
				SessionManager sessionManager = config.getHostServer().getSessionManager();
				GGXSession session = sessionManager.getSession(pack.getSession().getSessonId());
				if (session != null) {
					pack.setSession(session);
					session.send(pack);
				}
				
			}
		});
		
		

		this.serviceClient.registerController(new MessageController() {

			@GGXAction
			public void handle(RouterRedirectMessageToOtherRouterServicesResp resp, GGXSession session) {
				//监听session与路由服务绑定变更
				
				String sessionId = session.getSessonId();
				
				GGXCoreServer hostServer = config.getHostServer();
				SessionManager hostSessionManager = hostServer.getSessionManager();
				GGXSession hostSession = hostSessionManager.getSession(sessionId);
				
				RouterServiceManager routerServiceManager = RouterService.this.config.getRouterServiceManager();
				RouterService changeRouterService = routerServiceManager.getService(serviceGroupId, resp.getServiceId());
				if (changeRouterService == null) {
					hostServer.emitEvent(new EventData<>(hostSession, RouterClientEvents.RoutingMessage.MESSAGE_UNREACHABLE, null));
					return;
				}
				RouterServiceGroup serviceGroup = routerServiceManager.getServiceGroup(serviceGroupId);
				RouterServiceLoadbalancer routerServiceLoadbalancer = serviceGroup.getRouterServiceLoadblancer();
				
				routerServiceLoadbalancer.changeSessionBinding(resp.getTranferSessionId(), changeRouterService);
				
				Pack pack = new Pack();
				pack.setAction(resp.getAction());
				pack.setMessage(resp.getMessage());
				pack.setSerializeType(resp.getSerializeType());
				pack.setSession(hostSession);
				
				changeRouterService.dispatch(pack);
			}
			
			@GGXAction
			public void handle(RouterSessionDisconnectTransferResp resp) {
				//监听session断开回传
				String tranferSessionId = resp.getTranferSessionId();
				
				//判断是否继续处理会话断开推送传递
				if (RouterService.this.config.isSessionDisconnectTransferResponseEnabled()) {
					
					SessionManager hostServersessionManager = config.getHostServer().getSessionManager();
					GGXSession hostServerSession = hostServersessionManager.getSession(tranferSessionId);
					if (hostServerSession != null) {
						hostServerSession.addAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, RouterSessionDisconnectTransferType.RESP);
						hostServerSession.send(resp).addListener(f -> {
							hostServerSession.disconnect();
						});
					}
				}
			}
				
			
		});
		
		sessionGroupClient.start();
		
	}

	
	/**
	 * 转发消息
	 * 
	 * @param pack
	 * @author zai
	 * 2019-11-07 17:53:00
	 */
	public GGXFuture dispatch(Pack pack) {
		if (!isAvailable()) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		if (isShutdown()) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		GGXSession routingSession = pack.getSession();
		String routingSessonId = pack.getSession().getSessonId();
		
		
		SessionManager serviceClientSessionManager = this.serviceClient.getSessionManager();
		
		
		GGXSession serviceClientSession = serviceClientSessionManager.randomGetSession();
		RouteMessageReq req = new RouteMessageReq();
		req.setAction(pack.getAction());
		req.setMessage(pack.getMessage());
		req.setTranferSessionId(routingSessonId);
		req.setSerializeType(pack.getSerializeType());
		
		routingSession.addDisconnectListener(sesssion -> {
			Integer tranType = sesssion.getAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, Integer.class);
			if (tranType != null && tranType == RouterSessionDisconnectTransferType.RESP) {
				serviceClientSession.disconnect();
				return;
			}
			//判断是否开启会话断开传递请求
			if (this.config.isSessionDisconnectTransferRequestEnabled()) {
				//传递会话断开请求
				serviceClientSession.send(new RouterSessionDisconnectTransferReq(sesssion.getSessonId()));
			}
		});
		
		return serviceClientSession.send(req);
		
	}
	

	public boolean isAvailable() {
		if (shutdown) {
			return false;
		}
		if (this.sessionGroupClient != null) {
			return this.sessionGroupClient.getAvaliableConnections() > 0;
		}
		return false;
	}

	
	/**
	 * 关闭
	 * 
	 * @author zai
	 * 2019-11-07 15:51:04
	 */
	public void shutdown() {
		this.executor.submitTask(() -> {
			
			if (this.shutdown) {
				return;
			}
			this.shutdown = true;
			this.sessionGroupClient.shutdown(false);
			for (RouterServiceShutdownListener listener : shutdownListeners) {
				try {
					listener.onShutdown(this);
				} catch (Exception e) {
					GGXLogUtil.getLogger(this).error("RouterServiceShutdownListener ERROR!", e);
				}
				
			}
		});
	}
	
	public void addShutdownListener(RouterServiceShutdownListener listener) {
		this.executor.submitTask(() -> {
			if (shutdown) {
				listener.onShutdown(this);
				return;
			}
			this.shutdownListeners.add(listener);
		});
	}

	
	public String getServiceId() {
		return this.serviceId;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	

	public String getExtraData(String key) {
		return this.customData.get(key);
	}
	
	public void removeExtraData(String key) {
		this.customData.remove(key);
	}
	public void addExtraData(String key, String data) {
		this.customData.put(key, data);
	}
	public void addAllExtraData(Map<String, String> extraData) {
		this.customData.putAll(extraData);
	}
	public void replaceExtraData(Map<String, String> extraData) {
		this.customData.clear();
		this.customData.putAll(extraData);
	}
	
	public Map<String, String> getExtraDatas() {
		return customData;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
	public boolean isShutdown() {
		return shutdown;
	}
	public String getServiceGroupId() {
		return serviceGroupId;
	}
	
	public void setServiceGroupId(String servcieName) {
		this.serviceGroupId = servcieName;
	}
	
	public AtomicInteger getLoad() {
		return load;
	}

	public String getActionIdPrefix() {
		return actionIdPrefix;
	}

	public void setActionIdPrefix(String actionIdPrefix) {
		this.actionIdPrefix = actionIdPrefix;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	
	

}
