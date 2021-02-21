package com.ggx.server.starter;

import java.util.List;

import com.ggx.common.message.EventbusMessage;
import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGXSession;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.service.group.RpcServiceGroup;
import com.ggx.server.starter.config.GGXServerConfig;
import com.ggx.server.starter.config.module.GGXCoreServerConfigModel;
import com.ggx.server.starter.config.module.GGXEventbusServerConfigModel;
import com.ggx.server.starter.config.module.GGXGatewayConfigModel;
import com.ggx.server.starter.config.module.GGXLoadbalancerConfigModel;
import com.ggx.server.starter.config.module.GGXRegistryServerConfigModel;
import com.ggx.server.starter.config.module.GGXRoutingServerConfigModel;
import com.ggx.server.starter.config.module.GGXRpcServiceConfigModel;
import com.ggx.server.starter.config.module.GGXServiceClientConfigModel;
import com.ggx.server.starter.config.sub.GGXServerRpcConfigSubModel;
import com.ggx.server.starter.core.GGXCoreServerStarter;
import com.ggx.server.starter.eventbus.GGXEventbusServerStarter;
import com.ggx.server.starter.eventbus.GGXServiceClientStarter;
import com.ggx.server.starter.gateway.GGXGatewayStarter;
import com.ggx.server.starter.loadbalancer.GGXLoadbalancerServerStarter;
import com.ggx.server.starter.registry.GGXRegistryServerStarter;
import com.ggx.server.starter.rpc.GGXRpcServiceStarter;
import com.ggx.server.starter.service.GGXRoutingServiceStarter;

public class GGXServer implements GGXServerStarter {

	private GGXServerStarter serverStarter;

	private GGXServerConfig config;

	public GGXServer(GGXServerConfig config) {
		this.config = config;
		init();
	}

	private void init() {
		if (config.getCoreServer() != null && config.getCoreServer().isEnabled()) {
			GGXCoreServerConfigModel configModel = config.getCoreServer();
			GGXCoreServerStarter ggxCoreServerStarter = new GGXCoreServerStarter();
			ggxCoreServerStarter.setCoreServerConfig(configModel.getCore());
			if (configModel.getEventbus() != null && configModel.getEventbus().getClient() != null) {
				ggxCoreServerStarter.setEventbusGroupClientConfig(ggxCoreServerStarter.getEventbusGroupClientConfig());
			}
			if (configModel.getRpc() != null && configModel.getRpc().getClient() != null) {
				ggxCoreServerStarter.setRpcClientConfig(configModel.getRpc().getClient());
			}
			ggxCoreServerStarter.getCoreServerConfig().setScanPackages(config.getScanPackages());
			ggxCoreServerStarter.setRegistryClientConfig(ggxCoreServerStarter.getRegistryClientConfig());
			ggxCoreServerStarter.init();
			this.serverStarter = ggxCoreServerStarter;
			return;
		}
		if (config.getLoadbalancer() != null && config.getLoadbalancer().isEnabled()) {
			GGXLoadbalancerConfigModel configModel = config.getLoadbalancer();
			GGXLoadbalancerServerStarter ggxLoadbalancerServerStarter = new GGXLoadbalancerServerStarter();
			ggxLoadbalancerServerStarter.setRouterServerConfig(configModel.getRouter().getServer());
			ggxLoadbalancerServerStarter.setRouterClientConfig(configModel.getRouter().getClient());
			if (configModel.getEventbus() != null && configModel.getEventbus().getClient() != null) {
				ggxLoadbalancerServerStarter.setEventbusGroupClientConfig(configModel.getEventbus().getClient());
			}
			if (configModel.getRpc() != null && configModel.getRpc().getClient() != null) {
				ggxLoadbalancerServerStarter.setRpcClientConfig(configModel.getRpc().getClient());
			}
			ggxLoadbalancerServerStarter.setRegistryClientConfig(configModel.getRegistry().getClient());
			ggxLoadbalancerServerStarter.init();
			this.serverStarter = ggxLoadbalancerServerStarter;
			return;
		}
		if (config.getRoutingService() != null && config.getRoutingService().isEnabled()) {
			GGXRoutingServerConfigModel configModel = config.getRoutingService();
			GGXRoutingServiceStarter ggxRoutingServiceStarter = new GGXRoutingServiceStarter();
			ggxRoutingServiceStarter.setRouterServerConfig(configModel.getRouter().getServer());
			if (configModel.getEventbus() != null && configModel.getEventbus().getClient() != null) {
				ggxRoutingServiceStarter.setEventbusGroupClientConfig(configModel.getEventbus().getClient());
			}
			if (configModel.getRpc() != null) {
				if (configModel.getRpc().getClient() != null) {
					ggxRoutingServiceStarter.setRpcClientConfig(configModel.getRpc().getClient());
				}
				if (configModel.getRpc().getServer() != null) {
					ggxRoutingServiceStarter.setRpcServerConfig(configModel.getRpc().getServer());
				}
			}
			configModel.getRouter().getServer().setScanPackages(config.getScanPackages());
			ggxRoutingServiceStarter.setRegistryClientConfig(configModel.getRegistry().getClient());
			ggxRoutingServiceStarter.init();
			this.serverStarter = ggxRoutingServiceStarter;
			return;
		}
		if (config.getRegistryServer() != null && config.getRegistryServer().isEnabled()) {
			GGXRegistryServerConfigModel configModel = config.getRegistryServer();
			GGXRegistryServerStarter ggxRegistryServerStarter = new GGXRegistryServerStarter();
			ggxRegistryServerStarter.setRegistryServerConfig(configModel.getRegistry().getServer());
			ggxRegistryServerStarter.init();
			this.serverStarter = ggxRegistryServerStarter;
			return;
		}
		if (config.getGateway() != null && config.getGateway().isEnabled()) {
			GGXGatewayConfigModel configModel = config.getGateway();
			GGXGatewayStarter ggxGatewayStarter = new GGXGatewayStarter();
			ggxGatewayStarter.setCoreServerConfig(configModel.getCore());
			ggxGatewayStarter.setRegistryClientConfig(configModel.getRegistry().getClient());
			if (configModel.getEventbus() != null && configModel.getEventbus().getClient() != null) {
				ggxGatewayStarter.setEventbusGroupClientConfig(configModel.getEventbus().getClient());
			}
			if (configModel.getRpc() != null && configModel.getRpc().getClient() != null) {
				ggxGatewayStarter.setRpcClientConfig(configModel.getRpc().getClient());
			}
			if (configModel.getRpc().getServer() != null) {
				ggxGatewayStarter.setRpcServerConfig(configModel.getRpc().getServer());
			}
			configModel.getCore().setScanPackages(config.getScanPackages());
			RouterClientConfig routerClientConfig = configModel.getRouter().getClient();
			ggxGatewayStarter.setRouterClientConfig(routerClientConfig);
			ggxGatewayStarter.init();
			this.serverStarter = ggxGatewayStarter;
			return;

		}
		if (config.getServiceClient() != null && config.getServiceClient().isEnabled()) {
			GGXServiceClientConfigModel configModel = config.getServiceClient();
			GGXServiceClientStarter ggxEventbusClientStarter = new GGXServiceClientStarter();
			ggxEventbusClientStarter.setRegistryClientConfig(configModel.getRegistry().getClient());
			if (configModel.getEventbus() != null && configModel.getEventbus().getClient() != null) {
				ggxEventbusClientStarter.setEventbusGroupClientConfig(configModel.getEventbus().getClient());
			}
			if (configModel.getRpc() != null && configModel.getRpc().getClient() != null) {
				ggxEventbusClientStarter.setRpcClientConfig(configModel.getRpc().getClient());
			}
			ggxEventbusClientStarter.init();
			this.serverStarter = ggxEventbusClientStarter;
			return;
		}

		if (config.getEventbusServer() != null && config.getEventbusServer().isEnabled()) {
			GGXEventbusServerConfigModel configModel = config.getEventbusServer();
			GGXEventbusServerStarter ggxEventbusServerStarter = new GGXEventbusServerStarter();
			ggxEventbusServerStarter.setRegistryClientConfig(configModel.getRegistry().getClient());
			if (configModel.getEventbus() != null && configModel.getEventbus().getServer() != null) {
				ggxEventbusServerStarter.setEventbusServerConfig(configModel.getEventbus().getServer());
			}
			ggxEventbusServerStarter.init();
			this.serverStarter = ggxEventbusServerStarter;
			return;
		}

		if (config.getRpcService() != null && config.getRpcService().isEnabled()) {
			GGXRpcServiceConfigModel configModel = config.getRpcService();
			GGXRpcServiceStarter ggxRpcServiceStarter = new GGXRpcServiceStarter();
			ggxRpcServiceStarter.setRpcServerConfig(configModel.getRpc().getServer());
			ggxRpcServiceStarter.setRegistryClientConfig(configModel.getRegistry().getClient());

			if (configModel.getRpc() != null && configModel.getRpc().getClient() != null) {
				ggxRpcServiceStarter.setRpcClientConfig(configModel.getRpc().getClient());
			}
			ggxRpcServiceStarter.init();
			this.serverStarter = ggxRpcServiceStarter;
			return;
		}
	}

	public GGXFuture<?> routeMessage(String serviceId, Message message, GGXSession session) {
		return this.serverStarter.routeMessage(null, serviceId, message, session);
	}

	@Override
	public GGXFuture<?> routeMessage(String groupId, String serviceId, Message message, GGXSession session) {
		return this.serverStarter.routeMessage(groupId, serviceId, message, session);
	}
	

	@Override
	public void registerRouterTransferSessionAttr(String key, Class<?> clazz) {
		this.serverStarter.registerRouterTransferSessionAttr(key, clazz);
		
	}
	
	@Override
	public RouterServiceGroup getDefaultRouterServiceGroup(String serviceGroupId) {
		return this.serverStarter.getDefaultRouterServiceGroup(serviceGroupId);
	}

	@Override
	public void publishEventbusMessage(EventbusMessage message) {
		this.serverStarter.publishEventbusMessage(message);
	}

	@Override
	public void registerSubscriberController(Object controller) {
		this.serverStarter.registerSubscriberController(controller);
	}

	@Override
	public void registerRpcService(Class<?> serviceInterface, Object serviceObj) {
		this.serverStarter.registerRpcService(serviceInterface, serviceObj);

	}

	@Override
	public Object registerRpcClient(Class<?> serviceInterface, Object fallbackObj) {
		return this.serverStarter.registerRpcClient(serviceInterface, fallbackObj);
	}
	

	@Override
	public void start() {
		serverStarter.start();
	}

	@Override
	public GGXFuture<?> shutdown() {
		return serverStarter.shutdown();
	}

	@Override
	public GGXCore getGGXCore() {
		return this.serverStarter;
	}

	public GGXServerConfig getConfig() {
		return config;
	}

	@Override
	public RegistryClient getRegistryClient() {
		return this.serverStarter.getRegistryClient();
	}

	public List<RpcServiceGroup> getAllRpcServiceGroup() {
		GGXServerRpcConfigSubModel rpc = this.config.getServiceClient().getRpc();
		if (rpc == null) {
			return null;
		}
		RpcClientConfig client = rpc.getClient();
		if (client == null) {
			return null;
		}
		return client.getServiceManager().getList();
	}




}
