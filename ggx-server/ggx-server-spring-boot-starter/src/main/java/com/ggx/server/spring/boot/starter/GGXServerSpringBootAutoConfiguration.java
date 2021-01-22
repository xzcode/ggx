package com.ggx.server.spring.boot.starter;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationFailedEventListener;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationPrepareEventListener;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationStartedEventListener;
import com.ggx.server.spring.boot.starter.rpc.GGXRpcScannerRegistrar;
import com.ggx.server.spring.boot.starter.support.GGXAnnotationComponentScanner;
import com.ggx.server.spring.boot.starter.support.GGXSpringBeanGenerator;
import com.ggx.server.starter.GGXServer;
import com.ggx.server.starter.config.GGXServerConfig;
import com.ggx.server.starter.config.module.BaseModuleConfig;
import com.ggx.server.starter.config.module.GGXCoreServerConfigModel;
import com.ggx.server.starter.config.module.GGXEventbusServerConfigModel;
import com.ggx.server.starter.config.module.GGXGatewayConfigModel;
import com.ggx.server.starter.config.module.GGXLoadbalancerConfigModel;
import com.ggx.server.starter.config.module.GGXRegistryServerConfigModel;
import com.ggx.server.starter.config.module.GGXRoutingServerConfigModel;
import com.ggx.server.starter.config.module.GGXRpcServiceConfigModel;
import com.ggx.server.starter.config.module.GGXServiceClientConfigModel;
import com.ggx.server.starter.config.sub.GGXServerRpcConfigSubModel;

@Configuration
@ConditionalOnProperty(prefix = "ggx.server", name = "enabled", havingValue = "true")
public class GGXServerSpringBootAutoConfiguration implements ApplicationContextAware {

	protected ApplicationContext applicationContext;

	@Bean
	public static GGXAnnotationComponentScanner ggxAnnotationComponentScanner() {
		return new GGXAnnotationComponentScanner();
	}

	@Bean
	public static GGXRpcScannerRegistrar rpcBeanDefinitionRegistrar() {
		return new GGXRpcScannerRegistrar();
	}

	@Bean
	public static RpcClientConfig rpcClientConfig() {
		return new RpcClientConfig();
	}

	@Bean
	public static RpcServerConfig rpcServerConfig() {
		return new RpcServerConfig();
	}

	@Bean
	public GGXServer ggxserver(GGXServerConfig ggxserverConfig) {
		GGXServer ggxserver = new GGXServer(ggxserverConfig);
		return ggxserver;
	}
	

	public void handleCommonConfigModel(
		BaseModuleConfig configModel, 
		RpcClientConfig rpcClientConfig,
		RpcServerConfig rpcServerConfig
		) {
		configModel.setRpc(new GGXServerRpcConfigSubModel());
		configModel.setRpc(new GGXServerRpcConfigSubModel());
		configModel.getRpc().setClient(rpcClientConfig);
		configModel.getRpc().setServer(rpcServerConfig);
	}

	@ConfigurationProperties(prefix = "ggx.server")
	@Bean
	public GGXServerConfig ggxserverConfig(
		RpcClientConfig rpcClientConfig, 
		RpcServerConfig rpcServerConfig,
		@Nullable GGXRegistryServerConfigModel ggxRegistryServerConfigModel,
		@Nullable GGXGatewayConfigModel ggxGatewayConfigModel,
		@Nullable GGXCoreServerConfigModel ggxCoreServerConfigModel,
		@Nullable GGXLoadbalancerConfigModel ggxLoadbalancerConfigModel,
		@Nullable GGXRoutingServerConfigModel ggxRoutingServerConfigModel,
		@Nullable GGXServiceClientConfigModel ggxServiceClientConfigModel,
		@Nullable GGXEventbusServerConfigModel ggxEventbusServerConfigModel,
		@Nullable GGXRpcServiceConfigModel ggxRpcServiceConfigModel
		
		) {

		GGXServerConfig config = new GGXServerConfig();
		config.setCoreServer(ggxCoreServerConfigModel);
		config.setEventbusServer(ggxEventbusServerConfigModel);
		config.setGateway(ggxGatewayConfigModel);
		config.setLoadbalancer(ggxLoadbalancerConfigModel);
		config.setRegistryServer(ggxRegistryServerConfigModel);
		config.setRoutingService(ggxRoutingServerConfigModel);
		config.setRpcService(ggxRpcServiceConfigModel);
		config.setServiceClient(ggxServiceClientConfigModel);

		return config;
	}

	@ConfigurationProperties(prefix = "ggx.registry-server")
	@ConditionalOnProperty(prefix = "ggx.registry-server", name = "enabled", havingValue = "true")
	@Bean
	public GGXRegistryServerConfigModel ggxRegistryServerConfigModel(
		RpcClientConfig rpcClientConfig,
		RpcServerConfig rpcServerConfig
		) {

		GGXRegistryServerConfigModel configModel = new GGXRegistryServerConfigModel();
		handleCommonConfigModel(configModel, rpcClientConfig, rpcServerConfig);
		
		return configModel;
	}


	@ConfigurationProperties(prefix = "ggx.gateway")
	@ConditionalOnProperty(prefix = "ggx.gateway", name = "enabled", havingValue = "true")
	@Bean
	public GGXGatewayConfigModel ggxGatewayConfigModel(
		RpcClientConfig rpcClientConfig,
		RpcServerConfig rpcServerConfig
		) {

		GGXGatewayConfigModel configModel = new GGXGatewayConfigModel();
		handleCommonConfigModel(configModel, rpcClientConfig, rpcServerConfig);
		return configModel;
	}

	@ConfigurationProperties(prefix = "ggx.core-server")
	@ConditionalOnProperty(prefix = "ggx.core-server", name = "enabled", havingValue = "true")
	@Bean
	public GGXCoreServerConfigModel ggxCoreServerConfigModel(
		RpcClientConfig rpcClientConfig,
		RpcServerConfig rpcServerConfig
		) {

		GGXCoreServerConfigModel configModel = new GGXCoreServerConfigModel();
		handleCommonConfigModel(configModel, rpcClientConfig, rpcServerConfig);
		return configModel;
	}

	@ConfigurationProperties(prefix = "ggx.loadbalancer")
	@ConditionalOnProperty(prefix = "ggx.loadbalancer", name = "enabled", havingValue = "true")
	@Bean
	public GGXLoadbalancerConfigModel ggxLoadbalancerConfigModel(
		RpcClientConfig rpcClientConfig,
		RpcServerConfig rpcServerConfig
		) {

		GGXLoadbalancerConfigModel configModel = new GGXLoadbalancerConfigModel();
		handleCommonConfigModel(configModel, rpcClientConfig, rpcServerConfig);
		return configModel;
	}

	@ConfigurationProperties(prefix = "ggx.routing-service")
	@ConditionalOnProperty(prefix = "ggx.routing-service", name = "enabled", havingValue = "true")
	@Bean
	public GGXRoutingServerConfigModel ggxRoutingServerConfigModel(
		RpcClientConfig rpcClientConfig,
		RpcServerConfig rpcServerConfig
		) {

		GGXRoutingServerConfigModel configModel = new GGXRoutingServerConfigModel();
		handleCommonConfigModel(configModel, rpcClientConfig, rpcServerConfig);
		return configModel;
	}

	@ConfigurationProperties(prefix = "ggx.service-client")
	@ConditionalOnProperty(prefix = "ggx.service-client", name = "enabled", havingValue = "true")
	@Bean
	public GGXServiceClientConfigModel ggxServiceClientConfigModel(
		RpcClientConfig rpcClientConfig,
		RpcServerConfig rpcServerConfig
		) {

		GGXServiceClientConfigModel configModel = new GGXServiceClientConfigModel();
		handleCommonConfigModel(configModel, rpcClientConfig, rpcServerConfig);
		return configModel;
	}

	@ConfigurationProperties(prefix = "ggx.eventbus-server")
	@ConditionalOnProperty(prefix = "ggx.eventbus-server", name = "enabled", havingValue = "true")
	@Bean
	public GGXEventbusServerConfigModel ggxEventbusServerConfigModel(
		RpcClientConfig rpcClientConfig,
		RpcServerConfig rpcServerConfig
		) {

		GGXEventbusServerConfigModel configModel = new GGXEventbusServerConfigModel();
		handleCommonConfigModel(configModel, rpcClientConfig, rpcServerConfig);
		return configModel;
	}

	@ConfigurationProperties(prefix = "ggx.rpc-service")
	@ConditionalOnProperty(prefix = "ggx.rpc-service", name = "enabled", havingValue = "true")
	@Bean
	public GGXRpcServiceConfigModel ggxRpcServiceConfigModel(
		RpcClientConfig rpcClientConfig,
		RpcServerConfig rpcServerConfig
		) {

		GGXRpcServiceConfigModel configModel = new GGXRpcServiceConfigModel();
		handleCommonConfigModel(configModel, rpcClientConfig, rpcServerConfig);
		return configModel;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean
	public GGXSpringBeanGenerator ggxSpringBeanGenerator() {
		return new GGXSpringBeanGenerator();
	}

	@Bean
	public GGXServerSpringBootApplicationStartedEventListener applicationReadyEventListener() {
		return new GGXServerSpringBootApplicationStartedEventListener();
	}

	@Bean
	public GGXServerSpringBootApplicationFailedEventListener applicationFailedEventListener() {
		return new GGXServerSpringBootApplicationFailedEventListener();
	}

	@Bean
	public GGXServerSpringBootApplicationPrepareEventListener applicationPrepareEventListener() {
		return new GGXServerSpringBootApplicationPrepareEventListener();
	}

}
