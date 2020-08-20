package com.ggx.router.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.core.client.GGClient;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.filter.BeforeDeserializeFilter;
import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.filter.SendMessageFilter;
import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.event.RouterClientEvents;
import com.ggx.router.client.service.listener.RouterServiceShutdownListener;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;
import com.ggx.router.client.service.manager.RouterServiceManager;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;
import com.ggx.router.common.constant.RouterConstant;
import com.ggx.router.common.constant.RouterSessionDisconnectTransferType;
import com.ggx.router.common.message.req.RouterSessionDisconnectTransferReq;
import com.ggx.router.common.message.resp.RouterRedirectMessageToOtherRouterServicesResp;
import com.ggx.router.common.message.resp.RouterSessionDisconnectTransferResp;
import com.ggx.session.group.client.SessionGroupClient;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.ggx.session.group.client.session.GroupServiceClientSession;
import com.xzcode.ggserver.core.server.GGServer;

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
	
	//业务客户端
	protected GGClient serviceClient;
	
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
		
		
		//包日志输出控制
		if (!this.config.isPrintRouterInfo()) {
			sessionGroupClientConfig.getServiceClient().getConfig().getPackLogger().addPackLogFilter(pack -> {
				return false;
			});
		}
		
		this.serviceClient = sessionGroupClientConfig.getServiceClient();
		
		ReceiveMessageManager receiveMessageManager = serviceClient.getReceiveMessageManager();
		
		this.executor = this.serviceClient.getTaskExecutor().nextEvecutor();
		
		this.serviceClient.addFilter(new SendMessageFilter() {
			
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
		
		this.serviceClient.addFilter(new ReceiveMessageFilter() {
			
			@Override
			public boolean doFilter(MessageData<?> data) {
				String action = data.getAction();
				
				if (action.startsWith(RouterConstant.ACTION_ID_PREFIX)) {
					serviceClient.getReceiveMessageManager().handle(data);
					return false;
				}
				return true;
			}
		});
		
		this.serviceClient.addFilter(new BeforeDeserializeFilter() {
			
			@Override
			public boolean doFilter(Pack pack) {
				String actionString = pack.getActionString();
				if (receiveMessageManager.getMessageHandler(actionString) != null) {
					return true;
				}
				
				SessionManager sessionManager = config.getHostServer().getSessionManager();
				GGSession session = sessionManager.getSession(pack.getSession().getSessonId());
				if (session != null) {
					pack.setSession(session);
					session.send(pack);
				}
				return false;
			}
		});
		
		//监听session断开回传
		this.serviceClient.onMessage(RouterSessionDisconnectTransferResp.ACTION_ID, new MessageHandler<RouterSessionDisconnectTransferResp>() {

			@Override
			public void handle(MessageData<RouterSessionDisconnectTransferResp> messageData) {
				
					RouterSessionDisconnectTransferResp resp = messageData.getMessage();
					String tranferSessionId = resp.getTranferSessionId();
					SessionManager sessionManager = serviceClient.getSessionManager();
					GGSession session = sessionManager.getSession(tranferSessionId);
					if (session != null) {
						
						session.disconnect();
						
						
						//判断是否继续处理会话断开推送传递
						if (RouterService.this.config.isSessionDisconnectTransferResponseEnabled()) {
							
							SessionManager hostServersessionManager = config.getHostServer().getSessionManager();
							GGSession hostServerSession = hostServersessionManager.getSession(tranferSessionId);
							if (hostServerSession != null) {
								hostServerSession.addAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, RouterSessionDisconnectTransferType.RESP);
								hostServerSession.send(resp).addListener(f -> {
									hostServerSession.disconnect();
								});
							}
						}
						
					}
				}
				
			
		});
		
		

		//监听session与路由服务绑定变更
		this.serviceClient.onMessage(RouterRedirectMessageToOtherRouterServicesResp.ACTION_ID, new MessageHandler<RouterRedirectMessageToOtherRouterServicesResp>() {

			@Override
			public void handle(MessageData<RouterRedirectMessageToOtherRouterServicesResp> messageData) {
				
				GGSession session = messageData.getSession();
				String sessionId = session.getSessonId();
				RouterRedirectMessageToOtherRouterServicesResp message = messageData.getMessage();
				
				GGServer hostServer = config.getHostServer();
				SessionManager hostSessionManager = hostServer.getSessionManager();
				GGSession hostSession = hostSessionManager.getSession(sessionId);
				
				RouterServiceManager routerServiceManager = RouterService.this.config.getRouterServiceManager();
				RouterService changeRouterService = routerServiceManager.getService(serviceGroupId, message.getServiceId());
				if (changeRouterService == null) {
					hostServer.emitEvent(new EventData<>(hostSession, RouterClientEvents.RoutingMessage.MESSAGE_UNREACHABLE, null));
					return;
				}
				RouterServiceGroup serviceGroup = routerServiceManager.getServiceGroup(serviceGroupId);
				RouterServiceLoadblancer routerServiceLoadblancer = serviceGroup.getRouterServiceLoadblancer();
				
				
				
				
				routerServiceLoadblancer.changeSessionBinding(message.getSessionId(), changeRouterService);
				
				Pack pack = new Pack();
				pack.setAction(message.getAction());
				pack.setMessage(message.getMessage());
				pack.setSerializeType(message.getSerializeType());
				
				pack.setSession(hostSession);
				
				
				serviceGroup.dispatch(pack);
				
				
			
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
	public GGFuture dispatch(Pack pack) {
		if (!isAvailable()) {
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		if (isShutdown()) {
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		GGSession routeSession = pack.getSession();
		String routeSessonId = pack.getSession().getSessonId();
		
		
		SessionManager serviceClientSessionManager = this.serviceClient.getSessionManager();
		
		GGSession serviceClientSession = serviceClientSessionManager.getSession(routeSessonId);
		
		if (serviceClientSession == null) {
			GGSessionGroupManager sessionGroupManager = this.sessionGroupClient.getConfig().getSessionGroupManager();
			serviceClientSession = new GroupServiceClientSession(routeSessonId, this.sessionGroupClient.getConfig().getSessionGroupId(), sessionGroupManager, this.serviceClient.getConfig());
			GGSession addSessionIfAbsent = serviceClientSessionManager.addSessionIfAbsent(serviceClientSession);
			if (addSessionIfAbsent != null) {
				serviceClientSession = addSessionIfAbsent;
			}else {
				GGSession finalServiceClientSession = serviceClientSession;
				//服务session断开后，通知下一个路由节点session已断开
				routeSession.addDisconnectListener(sesssion -> {
					Integer tranType = sesssion.getAttribute(RouterConstant.ROUTER_SESSION_DISCONNECT_TRANSFER_TYPE_SESSION_KEY, Integer.class);
					if (tranType != null && tranType == RouterSessionDisconnectTransferType.RESP) {
						finalServiceClientSession.disconnect();
						return;
					}
					
					//判断是否开启会话断开传递请求
					if (this.config.isSessionDisconnectTransferReuestEnabled()) {
						//传递会话断开请求
						finalServiceClientSession.send(new RouterSessionDisconnectTransferReq(sesssion.getSessonId())).addListener(f -> {
							//断开当前session
							finalServiceClientSession.disconnect();
						});
					}else {
						finalServiceClientSession.disconnect();
					}
				});
			}
		}
		return serviceClientSession.send(pack);
		
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
					GGLoggerUtil.getLogger(this).error("RouterServiceShutdownListener ERROR!", e);
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
