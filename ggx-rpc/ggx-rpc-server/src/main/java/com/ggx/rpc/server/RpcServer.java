package com.ggx.rpc.server;

import java.nio.charset.Charset;

import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.rpc.common.constant.RpcConstant;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.rpc.server.handler.RpcReqHandler;

public class RpcServer implements GGXCore{
	
	private RpcServerConfig config;
	private GGXCoreServer serviceServer;
	
	public RpcServer(RpcServerConfig config) {
		super();
		this.config = config;
	}

	public GGXFuture start() {
		
		SessionGroupServerConfig sessionServerConfig = new SessionGroupServerConfig();
		sessionServerConfig.setAuthToken(this.config.getAuthToken());
		sessionServerConfig.setEnableServiceServer(true);
		sessionServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionServerConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		sessionServerConfig.setWorkThreadFactory(new GGXThreadFactory("ggx-rpc-serv-", false));
		sessionServerConfig.setPortChangeStrategy(this.config.getPortChangeStrategy());
		sessionServerConfig.setChangeAndRebootIfPortInUse(this.config.isChangeAndRebootIfPortInUse());
		sessionServerConfig.setBootWithRandomPort(this.config.isBootWithRandomPort());
		
		
		SessionGroupServer sessionGroupServer = new SessionGroupServer(sessionServerConfig);
		this.config.setSessionGroupServer(sessionGroupServer);
		
		this.serviceServer = sessionServerConfig.getServiceServer();
		
		this.serviceServer.onMessage(new RpcReqHandler(config));
		
		
		GGXFuture startFuture = sessionGroupServer.start();
		startFuture.addListener(f -> {
			if (f.isSuccess()) {
				//获取注册中心客户端
				RegistryClient registryClient = this.config.getRegistryClient();
				if (registryClient != null) {
					//添加自定义参数
					
					//添加自定义rpc组id
					registryClient.addCustomData(RpcConstant.REGISTRY_CUSTOM_RPC_GROUP_KEY, this.config.getRpcGroupId());
					//添加自定义rpc服务端端口
					registryClient.addCustomData(RpcConstant.REGISTRY_CUSTOM_RPC_PORT_KEY, String.valueOf(this.config.getPort()));
				}
			}
		});
		
		return startFuture;
	}
	
	public void shutdown() {
		this.config.getSessionGroupServer().shutdown();
	}
	
	public void setConfig(RpcServerConfig config) {
		this.config = config;
	}
	
	public RpcServerConfig getConfig() {
		return config;
	}

	@Override
	public SessionManager getSessionManager() {
		return null;
	}

	@Override
	public FilterManager getFilterManager() {
		return this.serviceServer ;
	}

	@Override
	public Charset getCharset() {
		return this.serviceServer.getCharset();
	}

	@Override
	public Serializer getSerializer() {
		return this.serviceServer.getSerializer();
	}

	@Override
	public ReceiveMessageManager getReceiveMessageManager() {
		return this.serviceServer.getReceiveMessageManager();
	}

	@Override
	public TaskExecutor getTaskExecutor() {
		return this.serviceServer.getTaskExecutor();
	}

	@Override
	public EventManager getEventManager() {
		return this.serviceServer.getEventManager();
	}
	
}
