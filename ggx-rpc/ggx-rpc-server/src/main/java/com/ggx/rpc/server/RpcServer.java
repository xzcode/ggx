package com.ggx.rpc.server;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.config.GGXCoreSupport;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.future.GGXFailedFuture;
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

public class RpcServer implements GGXCoreSupport {

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

	public GGXFuture<?> start() {
		String rpcServiceGroupId = this.config.getRpcServiceGroupId();
		
		if (rpcServiceGroupId == null || rpcServiceGroupId.isEmpty()) {
			GGXLogUtil.getLogger(this).error("'RpcServerConfig.rpcServiceGroupId' must not be 'null' or empty!!");
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		//是否使用自定义rpc线程池
		if (this.config.isUseCustomRpcThreadPool()) {
			if (this.config.getCustomRpcThreadPool() == null) {
				this.config.setCustomRpcThreadPool(new ScheduledThreadPoolExecutor(this.config.getCustomRpcThreadPoolSize(), new  GGXThreadFactory("ggx-rpc-custom-", false)));
			}
		}
		
		SessionGroupServerConfig sessionServerConfig = new SessionGroupServerConfig();
		sessionServerConfig.setAuthToken(this.config.getAuthToken());
		sessionServerConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		sessionServerConfig.setWorkThreadFactory(new GGXThreadFactory("ggx-rpc-serv-", false));
		sessionServerConfig.setPortChangeStrategy(this.config.getPortChangeStrategy());
		sessionServerConfig.setChangeAndRebootIfPortInUse(this.config.isChangeAndRebootIfPortInUse());
		sessionServerConfig.setBootWithRandomPort(this.config.isBootWithRandomPort());

		SessionGroupServer sessionGroupServer = new SessionGroupServer(sessionServerConfig);
		this.config.setSessionGroupServer(sessionGroupServer);

		this.serviceServer = sessionServerConfig.getServiceServer();
		GGXCoreServerConfig serviceServerConfig = this.serviceServer.getConfig();
		serviceServerConfig.setGgxComponent(true);
		this.serviceServer.registerMessageController(new RpcReqHandler(config));

		GGXFuture<?> startFuture = sessionGroupServer.start();
		startFuture.addListener(f -> {
			if (f.isSuccess()) {
				// 获取注册中心客户端
				RegistryClient registryClient = this.config.getRegistryClient();
				if (registryClient != null) {
					
					String rpcServiceId = this.config.getRpcServiceId();
					if (rpcServiceId == null || rpcServiceId.isEmpty()) {
						rpcServiceId = registryClient.getServiceId();
					}

					// 添加自定义rpc服务标识
					registryClient.addCustomData(RpcServiceCustomDataKeys.RPC_SERVICE, "true", false);

					// 添加自定义rpc组id
					registryClient.addCustomData(RpcServiceCustomDataKeys.RPC_SERVICE_ID, rpcServiceId, false);
					
					// 添加自定义rpc组id
					registryClient.addCustomData(RpcServiceCustomDataKeys.RPC_SERVICE_GROUP_ID, rpcServiceGroupId, false);

					// 添加自定义rpc服务端端口
					registryClient.addCustomData(RpcServiceCustomDataKeys.RPC_SERVICE_PORT, String.valueOf(this.config.getPort()), false);

					// 添加自定义rpc接口信息列表
					registryClient.addCustomData(RpcServiceCustomDataKeys.RPC_INTERFACE_INFO_LIST, GGXJsonUtil.toJson(invocationManager.getInterfaceInfoModelList()), false);

					registryClient.updateService();
				}
			}
		});

		return startFuture;
	}

	public GGXFuture<?> shutdown() {
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
	public GGXCore getGGXCore() {
		return this.serviceServer;
	}

}
