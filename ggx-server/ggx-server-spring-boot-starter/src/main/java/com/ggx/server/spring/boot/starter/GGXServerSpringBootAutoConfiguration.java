package com.ggx.server.spring.boot.starter;

import java.lang.reflect.Proxy;
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
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.common.annotation.GGXRpcInterface;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.server.spring.boot.starter.annotation.GGXController;
import com.ggx.server.spring.boot.starter.annotation.GGXEventHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageFilter;
import com.ggx.server.spring.boot.starter.annotation.GGXSubscriber;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationFailedEventListener;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationPrepareEventListener;
import com.ggx.server.spring.boot.starter.event.GGXServerSpringBootApplicationStartedEventListener;
import com.ggx.server.spring.boot.starter.rpc.RpcProxyFactoryBean;
import com.ggx.server.spring.boot.starter.support.GGXSpringBeanGenerator;
import com.ggx.server.starter.GGXServer;
import com.ggx.server.starter.config.GGXServerConfig;
import com.ggx.server.starter.config.GGXServerRpcConfigModel;
import com.ggx.util.logger.GGXLogUtil;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

@Configuration
@ConditionalOnProperty(prefix = "ggx", name = "enabled", havingValue = "true")
public class GGXServerSpringBootAutoConfiguration implements ApplicationContextAware {

	protected ApplicationContext applicationContext;
	
	protected RpcClientConfig rpcClientConfig = new RpcClientConfig();
	protected RpcServerConfig rpcServerConfig = new RpcServerConfig();
	
	@ConfigurationProperties(prefix = "ggx")
	@Bean
	public GGXServerConfig ggxserverConfig() {
		GGXServerConfig config = new GGXServerConfig();
		config.setRpc(new GGXServerRpcConfigModel());
		config.getRpc().setClient(rpcClientConfig);
		config.getRpc().setServer(rpcServerConfig);
		return config;
	}
	
	@Bean
	public GGXServer ggxserver() {
		GGXServer ggxserver = new GGXServer(ggxserverConfig());

		// 注册消息处理器
		Map<String, Object> messageControllers = this.applicationContext.getBeansWithAnnotation(GGXController.class);
		for (Entry<String, Object> entry : messageControllers.entrySet()) {
			Object obj = entry.getValue();
			ggxserver.register(obj);
		}

		// 注册事件处理器
		Map<String, Object> eventHandlers = this.applicationContext.getBeansWithAnnotation(GGXEventHandler.class);
		for (Entry<String, Object> entry : eventHandlers.entrySet()) {
			if (!(entry.getValue() instanceof EventListener)) {
				continue;
			}
			EventListener<?> obj = (EventListener<?>) entry.getValue();
			GGXEventHandler annotation = obj.getClass().getAnnotation(GGXEventHandler.class);
			if (annotation != null) {
				ggxserver.addEventListener(annotation.value(), obj);
			}
		}

		// 注册过滤器
		Map<String, Object> filters = this.applicationContext.getBeansWithAnnotation(GGXMessageFilter.class);
		for (Entry<String, Object> entry : filters.entrySet()) {
			if (!(entry.getValue() instanceof Filter)) {
				continue;
			}
			Filter<?> obj = (Filter<?>) entry.getValue();
			GGXMessageFilter annotation = obj.getClass().getAnnotation(GGXMessageFilter.class);
			if (annotation != null) {
				ggxserver.addFilter(new FilterInfo<>(obj, annotation.value()));
			}
		}

		// 注册eventbus事件订阅处理器
		Map<String, Object> subscribers = this.applicationContext.getBeansWithAnnotation(GGXSubscriber.class);
		for (Entry<String, Object> entry : subscribers.entrySet()) {
			if (!(entry.getValue() instanceof Subscriber)) {
				continue;
			}
			Subscriber obj = (Subscriber) entry.getValue();
			GGXSubscriber annotation = obj.getClass().getAnnotation(GGXSubscriber.class);
			if (annotation != null) {
				ggxserver.subscribe(annotation.value(), obj);
			}
		}
		
		
		return ggxserver;
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

		// 注册RPC服务
		String[] whitelistPackages = {getSpringBootEnterPackage()};
		ClassGraph classGraph = new ClassGraph();
		classGraph.enableAllInfo();
		if (whitelistPackages.length > 0) {
			classGraph.whitelistPackages(whitelistPackages);
		}
		
		try (ScanResult scanResult = classGraph.scan()) {
				ClassInfoList rpcInterfaceInfoList = scanResult.getClassesWithAnnotation(GGXRpcInterface.class.getName());
				for (ClassInfo info : rpcInterfaceInfoList) {
					String name = info.getName();
					Class<?> interfaceClass = Class.forName(name);
					GGXRpcInterface annotation = interfaceClass.getAnnotation(GGXRpcInterface.class);
					Class<?> fallbackClass = annotation.fallback();
					//String beanName = interfaceClass.getSimpleName();
					Object primary = null;
					try {
						primary = applicationContext.getBean(interfaceClass);
					} catch (Exception e) {
						GGXLogUtil.getLogger(this).debug("Interface [{}] has added a '@"+(GGXRpcInterface.class.getSimpleName())+"' annotation, but it dose not have any implementation class!", name);
					}
					if (
							primary == null
							||
							primary.getClass() == fallbackClass
						) {
						Object proxy = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass}, rpcClientConfig.getProxyInvocationHandler());
						rpcClientConfig.getProxyManager().register(interfaceClass, primary);
						registerRpcProxyBean(interfaceClass.getSimpleName(), interfaceClass, proxy, true);
					}else if(
							primary != null
							&& 
							primary.getClass() != fallbackClass) {
						rpcServerConfig.getInvocationManager().register(interfaceClass, primary);
					}
				}
		}catch (Exception e) {
			GGXLogUtil.getLogger(this).error("GGXServer Scan packages ERROR!", e);
		}
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
