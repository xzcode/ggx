package com.ggx.server.spring.boot.starter;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
import com.ggx.server.starter.config.GGXServerRpcConfigModel;

@Configuration
@ConditionalOnProperty(prefix = "ggx", name = "enabled", havingValue = "true")
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

	@ConfigurationProperties(prefix = "ggx")
	@Bean
	public GGXServerConfig ggxserverConfig(RpcClientConfig rpcClientConfig, RpcServerConfig rpcServerConfig) {

		GGXServerConfig config = new GGXServerConfig();
		config.setRpc(new GGXServerRpcConfigModel());
		config.getRpc().setClient(rpcClientConfig);
		config.getRpc().setServer(rpcServerConfig);
		return config;
	}

	@Bean
	public GGXServer ggxserver(GGXServerConfig ggxserverConfig) {
		GGXServer ggxserver = new GGXServer(ggxserverConfig);
		return ggxserver;
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
