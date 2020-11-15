package com.ggx.server.spring.boot.starter;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationFailedEventListener;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationPrepareEventListener;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationStartedEventListener;
import com.ggx.server.spring.boot.starter.support.GGXAnnotationComponentScanner;
import com.ggx.server.spring.boot.starter.support.GGXBeanDefinitionRegistryPostProcessor;
import com.ggx.server.spring.boot.starter.support.GGXSpringPrototypeBeanGenerator;
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
	public static GGXBeanDefinitionRegistryPostProcessor ggxBeanDefinitionRegistryPostProcessor() {
		return new GGXBeanDefinitionRegistryPostProcessor();
	}
	
	@ConfigurationProperties(prefix = "ggx")
	@Bean
	public GGXServerConfig ggxserverConfig() {
		
		GGXBeanDefinitionRegistryPostProcessor ggxBeanDefinitionRegistryPostProcessor = applicationContext.getBean(GGXBeanDefinitionRegistryPostProcessor.class);
		GGXServerConfig config = new GGXServerConfig();
		config.setRpc(new GGXServerRpcConfigModel());
		config.getRpc().setClient(ggxBeanDefinitionRegistryPostProcessor.getRpcClientConfig());
		config.getRpc().setServer(ggxBeanDefinitionRegistryPostProcessor.getRpcServerConfig());
		return config;
	}
	
	@Bean
	public GGXServer ggxserver() {
		GGXServer ggxserver = new GGXServer(ggxserverConfig());
		return ggxserver;
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	
	public boolean checkPackage(Class<?> checkClass, String[] scanPackages) {
		if (scanPackages != null && scanPackages.length > 0) {
			String className = checkClass.getName();
			for (String packa : scanPackages) {
				if (className.startsWith(packa)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	@Bean
	public GGXSpringPrototypeBeanGenerator ggxSpringBeanGenerator() {
		return new GGXSpringPrototypeBeanGenerator();
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
