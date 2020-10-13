package com.ggx.server.spring.boot.starter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.filter.Filter;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationFailedEventListener;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationPrepareEventListener;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationStartedEventListener;
import com.ggx.server.spring.boot.starter.rpc.RpcProxyFactoryBean;
import com.ggx.server.spring.boot.starter.support.GGXSpringBeanGenerator;
import com.ggx.server.spring.boot.starter.support.GGXBeanDefinitionRegistryPostProcessor;
import com.ggx.server.starter.GGXServer;
import com.ggx.server.starter.config.GGXServerConfig;
import com.ggx.server.starter.config.GGXServerRpcConfigModel;
import com.ggx.util.logger.GGXLogUtil;

@Configuration
@ConditionalOnProperty(prefix = "ggx", name = "enabled", havingValue = "true")
public class GGXServerSpringBootAutoConfiguration implements ApplicationContextAware {

	protected ApplicationContext applicationContext;
	
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
		GGXBeanDefinitionRegistryPostProcessor ggxBeanDefinitionRegistryPostProcessor = applicationContext.getBean(GGXBeanDefinitionRegistryPostProcessor.class);
		List<Object> controllers = ggxBeanDefinitionRegistryPostProcessor.getControllers();
		for (Object object : controllers) {
			ggxserver.registerController(object);
		}
		Map<String, EventListener<?>> eventhandlers = ggxBeanDefinitionRegistryPostProcessor.getEventhandlers();
		for (Entry<String, EventListener<?>> entry : eventhandlers.entrySet()) {
			ggxserver.addEventListener(entry.getKey(), entry.getValue());
		}
		Map<Object, Integer> messagefilters = ggxBeanDefinitionRegistryPostProcessor.getMessagefilters();
		for (Entry<Object, Integer> entry : messagefilters.entrySet()) {
			ggxserver.addFilter((Filter<?>) entry.getKey(), entry.getValue());
		}
		Map<String, Subscriber> eventsubscribers = ggxBeanDefinitionRegistryPostProcessor.getEventsubscribers();
		for (Entry<String, Subscriber> entry : eventsubscribers.entrySet()) {
			ggxserver.subscribe(entry.getKey(), entry.getValue());
		}
		
		return ggxserver;
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	

	public String getSpringBootEnterPackage() {
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			String enterClassName = stackTrace[stackTrace.length - 1].getClassName();
			Class<?> enterClass = Class.forName(enterClassName);
			if (enterClass != null) {
				return enterClass.getPackage().getName();
			}
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Get SpringBoot Enter Package Error!", e);
		}
		return null;
	}
		
	public void registerRpcProxyBean(String name, Class<?> clazz, Object proxy, boolean primary) {
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) this.applicationContext;

		if (applicationContext.containsBean(name)) {
			throw new RuntimeException("Duplicate bean name!");
		}

		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);

		GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
		beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, clazz);
		beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, proxy);
		beanDefinition.setBeanClass(RpcProxyFactoryBean.class);

		// 这里采用的是byType方式注入，类似的还有byName等
		beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
		beanDefinition.setPrimary(primary);
		beanDefinition.setAutowireCandidate(true);
		BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
		registry.registerBeanDefinition(name, beanDefinition);

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
