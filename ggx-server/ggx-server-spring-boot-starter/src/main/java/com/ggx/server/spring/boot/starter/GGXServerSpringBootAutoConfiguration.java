package com.ggx.server.spring.boot.starter;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ggx.server.spring.boot.starter.config.GGXServerConfigModel;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationFailedEventListener;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationReadyEventListener;
import com.ggx.server.starter.GGXServer;

@Configuration
public class GGXServerSpringBootAutoConfiguration implements ApplicationContextAware {

	protected ApplicationContext applicationContext;
	
	@ConfigurationProperties(prefix = "ggx")
	@Bean
	public GGXServerConfigModel ggxServerConfigModel() {
		return new GGXServerConfigModel();
	}
	
	@ConditionalOnProperty(havingValue = "ggx.enabled")
	@Bean
	public GGXServer ggxServer() {
		GGXServerConfigModel configModel = ggxServerConfigModel();
		GGXServer ggxserver = new GGXServer(configModel.getMode());
		return ggxserver;
	}
	
	@Bean
	public GGXSpringBeanGenerator ggxSpringBeanGenerator() {
		return new GGXSpringBeanGenerator();
	}
	@Bean
	public GGXServerSpringBootApplicationReadyEventListener applicationReadyEventListener() {
		return new GGXServerSpringBootApplicationReadyEventListener();
	}
	
	@Bean
	public GGXServerSpringBootApplicationFailedEventListener applicationFailedEventListener() {
		return new GGXServerSpringBootApplicationFailedEventListener();
	};

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	


}
