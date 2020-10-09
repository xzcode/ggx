package com.ggx.rpc.server;

import java.nio.charset.Charset;
import java.util.Set;

import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.actionid.ActionIdCacheManager;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.group.server.SessionGroupServer;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.registry.client.RegistryClient;
import com.ggx.rpc.common.constant.RpcServiceCustomDataKeys;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.rpc.server.handler.RpcReqHandler;
import com.ggx.rpc.server.invocation.InvocationManager;
import com.ggx.util.json.GGXJsonUtil;
import com.ggx.util.logger.GGXLogUtil;

public class RpcServer implements GGXCore{
	
	private RpcServerConfig config;
	private GGXCoreServer serviceServer;
	private InvocationManager invocationManager;
	
	public RpcServer(RpcServerConfig config) {
		this.config = config;
		this.invocationManager = config.getInvocationManager();
	}
	
	public void register(Class<?> serviceInterface, Object instance) {
		this.invocationManager.register(serviceInterface, instance);
		
	}

	public GGXFuture start() {
		String serviceGroupId = this.config.getServiceGroupId();
		
		if (serviceGroupId == null || serviceGroupId.isEmpty()) {
			GGXLogUtil.getLogger(this).error("'RpcServerConfig.serviceGroupId' must not be 'null' or empty!!");
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		
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
		GGXCoreServerConfig serviceServerConfig = this.serviceServer.getConfig();
		serviceServerConfig.setGgxComponent(true);
		this.serviceServer.onMessage(new RpcReqHandler(config));
	
		
		
		GGXFuture startFuture = sessionGroupServer.start();
		startFuture.addListener(f -> {
			if (f.isSuccess()) {
				//获取注册中心客户端
				RegistryClient registryClient = this.config.getRegistryClient();
				if (registryClient != null) {
					
					//添加自定义rpc组id
					registryClient.addCustomData(RpcServiceCustomDataKeys.RPC_SERVICE, "true");
					
					//添加自定义rpc服务端端口
					registryClient.addCustomData(RpcServiceCustomDataKeys.RPC_SERVICE_PORT, String.valueOf(this.config.getPort()));
					
					//添加自定义rpc接口信息列表
					registryClient.addCustomData(RpcServiceCustomDataKeys.RPC_INTERFACE_INFO_LIST, GGXJsonUtil.toJson(invocationManager.getInterfaceInfoModelList()));
				}
			}
		});
		
		return startFuture;
	}
	
	public GGXFuture shutdown() {
		if (this.config.getSessionGroupServer() != null) {
			return this.config.getSessionGroupServer().shutdown();			
		}
		return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
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

	@Override
	public ActionIdCacheManager getActionIdCacheManager() {
		return this.serviceServer.getActionIdCacheManager();
	}
	
}
