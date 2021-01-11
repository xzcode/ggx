package com.ggx.session.group.client;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.message.send.support.MakePackSupport;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.common.message.req.AuthReq;
import com.ggx.group.common.session.SessionGroupSessionFactory;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.ggx.session.group.client.handler.AnthRespHandler;
import com.ggx.session.group.client.handler.DataTransferRespHandler;
import com.ggx.session.group.client.handler.SessionGroupRegisterRespHandler;
import com.ggx.session.group.client.session.GroupServiceClientSession;
import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.thread.GGXThreadFactory;

/**
 * 会话组客户端
 *
 * @author zai 2020-04-08 11:47:15
 */
public class SessionGroupClient implements EventSupport, MakePackSupport{

	private SessionGroupClientConfig config;

	//当前可用连接数
	private AtomicInteger avaliableConnections = new AtomicInteger(0);
	
	
	private boolean shutdown;
	
	protected TaskExecutor singleThreadEvecutor;
	
	public SessionGroupClient(SessionGroupClientConfig config) {
		this.config = config;
		this.config.setSessionGroupClient(this);
		init();
	}
	
	
	private void init() {
		
		if (this.config.getWorkThreadFactory() == null) {
			this.config.setWorkThreadFactory(new GGXThreadFactory("gg-group-cli-", false));
		}
		
		GGXCoreClientConfig sessionClientConfig = new GGXCoreClientConfig();
		
		sessionClientConfig.setPingPongEnabled(true);
		sessionClientConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionClientConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionClientConfig.setWorkerGroupThreadFactory(this.config.getWorkThreadFactory());
		sessionClientConfig.setProtocolType(ProtocolTypeConstants.TCP);
		sessionClientConfig.setSessionFactory(new SessionGroupSessionFactory(sessionClientConfig));
		sessionClientConfig.setGgxComponent(true);
		
		if (this.config.getWorkEventLoopGroup() != null) {
			sessionClientConfig.setWorkerGroup(this.config.getWorkEventLoopGroup());
		}
		

		sessionClientConfig.init();
		
		GGSessionGroupManager sessionGroupManager = new GGSessionGroupManager(sessionClientConfig);
		this.config.setSessionGroupManager(sessionGroupManager);
		

		GGXCoreClient sessionClient = new GGXCoreClient(sessionClientConfig);
		this.config.setSessionClient(sessionClient);
		
		//获取一个单线程执行器
		this.singleThreadEvecutor = sessionClient.getTaskExecutor().nextEvecutor();


		sessionClient.registerMessageController(new AnthRespHandler(this.config));
		sessionClient.registerMessageController(new SessionGroupRegisterRespHandler(this.config));
		sessionClient.registerMessageController(new DataTransferRespHandler(this.config));

		//添加断开连接监听器
		sessionClient.addEventListener(GGXCoreEvents.Connection.CLOSED, ((EventData<Void> eventData) -> {
			avaliableConnections.decrementAndGet();
			
			SessionManager sessionManager = this.config.getServiceClient().getSessionManager();
			sessionManager.remove(eventData.getSession().getSessionId());
			
			//断开连接后，创建新连接
			this.config.getSessionGroupClient().connectOne(config.getServerHost(), config.getServerPort());
			
		}));
		
		
		//添加打开连接监听器
		sessionClient.addEventListener(GGXCoreEvents.Connection.OPENED, (EventData<Void> eventData) -> {
			
			avaliableConnections.incrementAndGet();
			
			//打开连接，发送认证
			GGXSession groupSession = eventData.getSession();
			groupSession.send(new AuthReq(config.getAuthToken()));
			
			this.config.getSessionGroupManager().addSession(this.config.getSessionGroupId(), groupSession);
			
			if (this.config.isEnableServiceClient()) {
				
				GGXCoreClientConfig serviceClientConfig = this.config.getServiceClient().getConfig();
				
				SessionManager sessionManager = serviceClientConfig.getSessionManager();
				
				GGXSession serviceServerSession = new GroupServiceClientSession(groupSession.getSessionId(), groupSession, this.config.getSessionGroupId(), sessionGroupManager, serviceClientConfig);
				GGXSession addSessionIfAbsent = sessionManager.addSessionIfAbsent(serviceServerSession);
				if (addSessionIfAbsent != null) {
					serviceServerSession = addSessionIfAbsent;
				}
				sessionManager.addSessionIfAbsent(serviceServerSession);
			}
			
			
		});
		
		
		if (this.config.isEnableServiceClient() && this.config.getServiceClient() == null) {
			
			GGXCoreClientConfig serviceClientConfig = new GGXCoreClientConfig();
			serviceClientConfig.setGgxComponent(true);
			serviceClientConfig.setWorkerGroup(sessionClientConfig.getWorkerGroup());
			serviceClientConfig.setActionIdPrefix(config.getServiceActionIdPrefix());
			serviceClientConfig.init();
			
			GGXCoreClient serviceClient = new GGXCoreClient(serviceClientConfig);
			this.config.setServiceClient(serviceClient);
			
		}
	}

	/**
	 * 启动客户端
	 *
	 * @author zai 2020-04-08 11:47:42
	 */
	public void start() {
		this.connect();
	}
	

	/**
	 * 进行连接操作
	 *
	 * @author zai 2020-04-08 11:46:06
	 */
	private void connect() {
		// 根据设置的连接数进行连接初始化
		int connectionSize = config.getConnectionSize();
		for (int i = 0; i < connectionSize; i++) {
			connectOne(config.getServerHost(), config.getServerPort());
		}
	}

	/**
	 * 进行一次连接
	 *
	 * @param host
	 * @param port
	 * @author zai 2020-04-08 11:45:53
	 */
	public void connectOne(String host, int port) {
		this.singleThreadEvecutor.submitTask(() -> {
			if (shutdown) {
				return;
			}
			GGXCoreClient ggclient = config.getSessionClient();
			ggclient.connect(host, port).addListener(f -> {
				this.singleThreadEvecutor.submitTask(() -> {
					if (shutdown) {
						return;
					}
					if (!f.isSuccess()) {
						// 连接失败，进行进行重连操作
						GGXLogUtil.getLogger(this).warn("SessionGroupClient Connect Server[{}:{}] Fail!", host, port);
						ggclient.schedule(config.getReconnectInterval(), () -> {
							connectOne(host, port);
						});
						return;
					}
					GGXLogUtil.getLogger(this).warn("SessionGroupClient Connect Server[{}:{}] Success!", host, port);
				});
				
			});
		});
	}
	
	/**
	 * 关闭客户端
	 *
	 * @param closeExecutors
	 * @author zai
	 * 2020-05-04 18:14:02
	 */
	public void shutdown(boolean closeExecutors) {
		this.singleThreadEvecutor.submitTask(() -> {
			if (shutdown) {
				return;
			}
			this.shutdown = true;
			GGXCoreClient sessionClient = config.getSessionClient();
			if (closeExecutors) {
				sessionClient.shutdown();
			}else {
				sessionClient.getSessionManager().disconnectAllSession();
			}
		});
		
	}
	
	
	public SessionGroupClientConfig getConfig() {
		return config;
	}
	
	public void setConfig(SessionGroupClientConfig config) {
		this.config = config;
	}


	@Override
	public EventManager getEventManager() {
		return this.config.getSessionClient();
	}


	@Override
	public Charset getCharset() {
		return this.config.getSessionClient().getCharset();
	}


	@Override
	public Serializer getSerializer() {
		return this.config.getSessionClient().getSerializer();
	}

	/**
	 * 获取当前可用连接数
	 *
	 * @return
	 * @author zai
	 * 2020-05-04 17:17:35
	 */
	public int getAvaliableConnections() {
		return avaliableConnections.get();
	}

}
