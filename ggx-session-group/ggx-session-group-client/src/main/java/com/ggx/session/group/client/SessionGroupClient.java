package com.ggx.session.group.client;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.core.client.GGClient;
import com.ggx.core.client.config.GGClientConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.handler.serializer.ISerializer;
import com.ggx.core.common.message.send.support.MakePackSupport;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.group.common.constant.GGSesssionGroupConstant;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.common.message.req.AuthReq;
import com.ggx.group.common.message.resp.AuthResp;
import com.ggx.group.common.message.resp.DataTransferResp;
import com.ggx.group.common.message.resp.SessionGroupRegisterResp;
import com.ggx.group.common.session.SessionGroupSessionFactory;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.ggx.session.group.client.handler.AnthRespHandler;
import com.ggx.session.group.client.handler.DataTransferRespHandler;
import com.ggx.session.group.client.handler.SessionGroupRegisterRespHandler;
import com.ggx.session.group.client.session.GroupServiceClientSession;

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
	
	public SessionGroupClient(SessionGroupClientConfig config) {
		this.config = config;
		config.setSessionGroupClient(this);
		init();
	}
	
	
	private void init() {
		
		if (this.config.getWorkThreadFactory() == null) {
			this.config.setWorkThreadFactory(new GGThreadFactory("gg-group-cli-", false));
		}
		
		GGClientConfig sessionClientConfig = new GGClientConfig();

		sessionClientConfig.setPingPongEnabled(true);
		sessionClientConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionClientConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionClientConfig.setWorkerGroupThreadFactory(this.config.getWorkThreadFactory());
		sessionClientConfig.setProtocolType(ProtocolTypeConstants.TCP);
		sessionClientConfig.setSessionFactory(new SessionGroupSessionFactory(sessionClientConfig));
		
		if (this.config.getWorkEventLoopGroup() != null) {
			sessionClientConfig.setWorkerGroup(this.config.getWorkEventLoopGroup());
		}
		
		if (!this.config.isPrintSessionGroupPackLog()) {
			sessionClientConfig.getPackLogger().addPackLogFilter(pack -> {
				String actionString = pack.getActionString();
				return !(actionString.startsWith(GGSesssionGroupConstant.ACTION_ID_PREFIX));
			});
		}

		sessionClientConfig.init();
		
		GGSessionGroupManager sessionGroupManager = new GGSessionGroupManager(sessionClientConfig);
		this.config.setSessionGroupManager(sessionGroupManager);
		

		GGClient sessionClient = new GGClient(sessionClientConfig);
		this.config.setSessionClient(sessionClient);


		sessionClient.onMessage(AuthResp.ACTION_ID, new AnthRespHandler(this.config));
		sessionClient.onMessage(SessionGroupRegisterResp.ACTION_ID, new SessionGroupRegisterRespHandler(this.config));
		sessionClient.onMessage(DataTransferResp.ACTION_ID, new DataTransferRespHandler(this.config));

		//添加断开连接监听器
		sessionClient.addEventListener(GGEvents.Connection.CLOSED, ((EventData<Void> eventData) -> {
			avaliableConnections.decrementAndGet();
			
			SessionManager sessionManager = this.config.getServiceClient().getSessionManager();
			sessionManager.remove(eventData.getSession().getSessonId());
			
			//断开连接后，创建新连接
			this.config.getSessionGroupClient().connectOne(config.getServerHost(), config.getServerPort());
			
		}));
		
		
		//添加打开连接监听器
		sessionClient.addEventListener(GGEvents.Connection.OPENED, (EventData<Void> eventData) -> {
			
			avaliableConnections.incrementAndGet();
			
			//打开连接，发送认证
			GGSession groupSession = eventData.getSession();
			groupSession.send(new AuthReq(config.getAuthToken()));
			
			this.config.getSessionGroupManager().addSession(this.config.getSessionGroupId(), groupSession);
			
			
			if (this.config.isEnableServiceClient()) {
				
				GGClientConfig serviceClientConfig = this.config.getServiceClient().getConfig();
				SessionManager sessionManager = serviceClientConfig.getSessionManager();
				
				GroupServiceClientSession serviceServerSession = new GroupServiceClientSession(groupSession.getSessonId(), this.config.getSessionGroupId(), sessionGroupManager, serviceClientConfig);
				GGSession addSessionIfAbsent = sessionManager.addSessionIfAbsent(serviceServerSession);
				if (addSessionIfAbsent != null) {
					serviceServerSession = (GroupServiceClientSession) addSessionIfAbsent;
				}
				sessionManager.addSessionIfAbsent(serviceServerSession);
			}
			
			
		});
		
		
		if (this.config.isEnableServiceClient() && this.config.getServiceClient() == null) {
			
			GGClientConfig serviceClientConfig = new GGClientConfig();
			serviceClientConfig.setWorkerGroup(sessionClientConfig.getWorkerGroup());
			serviceClientConfig.init();
			
			GGClient serviceClient = new GGClient(serviceClientConfig);
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
		if (shutdown) {
			return;
		}
		GGClient ggclient = config.getSessionClient();
		ggclient.connect(host, port).addListener(f -> {
			if (shutdown) {
				return;
			}
			if (!f.isSuccess()) {
				// 连接失败，进行进行重连操作
				GGLoggerUtil.getLogger(this).warn("SessionGroupClient Connect Server[{}:{}] Fail!", host, port);
				ggclient.schedule(config.getReconnectInterval(), () -> {
					connectOne(host, port);
				});
				return;
			}
			GGLoggerUtil.getLogger(this).warn("SessionGroupClient Connect Server[{}:{}] Success!", host, port);
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
		if (shutdown) {
			return;
		}
		this.shutdown = true;
		GGClient sessionClient = config.getSessionClient();
		if (closeExecutors) {
			sessionClient.shutdown();
		}else {
			sessionClient.getSessionManager().disconnectAllSession();
		}
		
	}
	
	
	public SessionGroupClientConfig getConfig() {
		return config;
	}
	
	public void setConfig(SessionGroupClientConfig config) {
		this.config = config;
	}


	@Override
	public EventManager getEventManagerImpl() {
		return this.config.getSessionClient();
	}


	@Override
	public Charset getCharset() {
		return this.config.getSessionClient().getCharset();
	}


	@Override
	public ISerializer getSerializer() {
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
