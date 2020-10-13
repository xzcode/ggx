package com.ggx.server.spring.boot.starter.support;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.filter.Filter;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.common.annotation.GGXRpcInterface;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.server.spring.boot.starter.annotation.GGXController;
import com.ggx.server.spring.boot.starter.annotation.GGXEventHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageFilter;
import com.ggx.server.spring.boot.starter.annotation.GGXSubscriber;
import com.ggx.server.spring.boot.starter.rpc.RpcProxyFactoryBean;
import com.ggx.util.logger.GGXLogUtil;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class GGXBeanDefinitionRegistryPostProcessor implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor{

	private ApplicationContext applicationContext;
	
	protected RpcClientConfig rpcClientConfig = new RpcClientConfig();
	protected RpcServerConfig rpcServerConfig = new RpcServerConfig();
	
	protected List<Object> controllers = new ArrayList<>();
	protected Map<String, EventListener<?>> eventhandlers = new HashMap<>();
	protected Map<Object, Integer> messagefilters = new HashMap<>();
	protected Map<String, Subscriber> eventsubscribers = new HashMap<>();

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		// 注册消息处理器
		Map<String, Object> messageControllers = beanFactory.getBeansWithAnnotation(GGXController.class);
		for (Entry<String, Object> entry : messageControllers.entrySet()) {
			Object obj = entry.getValue();
			controllers.add(obj);
		}

		// 注册事件处理器
		Map<String, Object> eventHandlers = beanFactory.getBeansWithAnnotation(GGXEventHandler.class);
		for (Entry<String, Object> entry : eventHandlers.entrySet()) {
			if (!(entry.getValue() instanceof EventListener)) {
				continue;
			}
			EventListener<?> obj = (EventListener<?>) entry.getValue();
			GGXEventHandler annotation = obj.getClass().getAnnotation(GGXEventHandler.class);
			if (annotation != null) {
				eventhandlers.put(annotation.value(), obj);
			}
		}

		// 注册过滤器
		Map<String, Object> filters = beanFactory.getBeansWithAnnotation(GGXMessageFilter.class);
		for (Entry<String, Object> entry : filters.entrySet()) {
			if (!(entry.getValue() instanceof Filter)) {
				continue;
			}
			Filter<?> obj = (Filter<?>) entry.getValue();
			GGXMessageFilter annotation = obj.getClass().getAnnotation(GGXMessageFilter.class);
			if (annotation != null) {
				int order = annotation.value();
				messagefilters.put(obj, order);
			}
		}

		// 注册eventbus事件订阅处理器
		Map<String, Object> subscribers = beanFactory.getBeansWithAnnotation(GGXSubscriber.class);
		for (Entry<String, Object> entry : subscribers.entrySet()) {
			if (!(entry.getValue() instanceof Subscriber)) {
				continue;
			}
			Subscriber obj = (Subscriber) entry.getValue();
			GGXSubscriber annotation = obj.getClass().getAnnotation(GGXSubscriber.class);
			if (annotation != null) {
				eventsubscribers.put(annotation.value(), obj);
			}
		}
				
		
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

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
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

	public RpcClientConfig getRpcClientConfig() {
		return rpcClientConfig;
	}
	public RpcServerConfig getRpcServerConfig() {
		return rpcServerConfig;
	}
	
	public List<Object> getControllers() {
		return controllers;
	}
	public Map<String, EventListener<?>> getEventhandlers() {
		return eventhandlers;
	}
	public Map<String, Subscriber> getEventsubscribers() {
		return eventsubscribers;
	}
	public Map<Object, Integer> getMessagefilters() {
		return messagefilters;
	}
}
