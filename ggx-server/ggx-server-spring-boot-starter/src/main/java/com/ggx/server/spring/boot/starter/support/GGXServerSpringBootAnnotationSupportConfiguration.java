package com.ggx.server.spring.boot.starter.support;

import java.lang.reflect.AnnotatedType;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.utils.GenericClassUtil;
import com.ggx.core.common.utils.MessageActionIdUtil;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.rpc.common.annotation.GGXRpcInterface;
import com.ggx.server.spring.boot.starter.annotation.GGXEventHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageFilter;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXSubscriber;
import com.ggx.server.spring.boot.starter.rpc.RpcProxyFactoryBean;
import com.ggx.server.starter.GGXServer;
import com.ggx.server.starter.config.GGXServerConfig;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

@Configuration
@ConditionalOnProperty(prefix = "ggx", name = "enabled", havingValue = "true")
public class GGXServerSpringBootAnnotationSupportConfiguration
		implements ApplicationContextAware, BeanPostProcessor {

	private ApplicationContext applicationContext;

	@Autowired
	private GGXServer ggxServer;

	private String[] basicPackage;

	public GGXServerSpringBootAnnotationSupportConfiguration() {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

		// 注册消息处理器
				Map<String, Object> messageHandlers = this.applicationContext.getBeansWithAnnotation(GGXMessageHandler.class);
				for (Entry<String, Object> entry : messageHandlers.entrySet()) {
					if (!(entry.getValue() instanceof MessageHandler)) {
						continue;
					}
					MessageHandler<?> obj = (MessageHandler<?>) entry.getValue();

					// 检查包路径
					if (!checkPackage(obj.getClass())) {
						continue;
					}
					GGXMessageHandler annotation = obj.getClass().getAnnotation(GGXMessageHandler.class);
					if (annotation != null) {
						String actionId = annotation.value();
						if (!actionId.isEmpty()) {
							ggxServer.onMessage(actionId, obj);
						} else {
							Class<?> genericClass = GenericClassUtil.getInterfaceGenericClass(obj.getClass());
							actionId = MessageActionIdUtil.generateClassNameDotSplitActionId(genericClass);
							ggxServer.onMessage(actionId, obj);
						}
					}
				}

				// 注册事件处理器
				Map<String, Object> eventHandlers = this.applicationContext.getBeansWithAnnotation(GGXEventHandler.class);
				for (Entry<String, Object> entry : eventHandlers.entrySet()) {
					if (!(entry.getValue() instanceof EventListener)) {
						continue;
					}
					EventListener<?> obj = (EventListener<?>) entry.getValue();
					// 检查包路径
					if (!checkPackage(obj.getClass())) {
						continue;
					}
					GGXEventHandler annotation = obj.getClass().getAnnotation(GGXEventHandler.class);
					if (annotation != null) {
						ggxServer.addEventListener(annotation.value(), obj);
					}
				}

				// 注册过滤器
				Map<String, Object> filters = this.applicationContext.getBeansWithAnnotation(GGXMessageFilter.class);
				for (Entry<String, Object> entry : filters.entrySet()) {
					if (!(entry.getValue() instanceof Filter)) {
						continue;
					}
					Filter<?> obj = (Filter<?>) entry.getValue();
					// 检查包路径
					if (!checkPackage(obj.getClass())) {
						continue;
					}
					GGXMessageFilter annotation = obj.getClass().getAnnotation(GGXMessageFilter.class);
					if (annotation != null) {
						ggxServer.addFilter(new FilterInfo<>(obj, annotation.value()));
					}
				}

				// 注册eventbus事件订阅处理器
				Map<String, Object> subscribers = this.applicationContext.getBeansWithAnnotation(GGXSubscriber.class);
				for (Entry<String, Object> entry : subscribers.entrySet()) {
					if (!(entry.getValue() instanceof Subscriber)) {
						continue;
					}
					Subscriber obj = (Subscriber) entry.getValue();
					// 检查包路径
					if (!checkPackage(obj.getClass())) {
						continue;
					}
					GGXSubscriber annotation = obj.getClass().getAnnotation(GGXSubscriber.class);
					if (annotation != null) {
						ggxServer.subscribe(annotation.value(), obj);
					}
				}

				// 注册RPC服务

				Map<String, Object> rpcServices = this.applicationContext.getBeansWithAnnotation(GGXRpcInterface.class);
				for (Entry<String, Object> entry : rpcServices.entrySet()) {
					Object obj = entry.getValue();
					Class<? extends Object> clazz = obj.getClass();
					AnnotatedType[] annotatedInterfaces = clazz.getAnnotatedInterfaces();
					for (AnnotatedType annotatedType : annotatedInterfaces) {
						Class<?> interfaceClass = (Class<?>) annotatedType.getType();
						GGXRpcInterface annotation = interfaceClass.getDeclaredAnnotation(GGXRpcInterface.class);
						if (annotation != null) {
							Class<?> fallbackClass = annotation.fallback();

							Object primaryObj = applicationContext.getBean(interfaceClass);
							if (fallbackClass != Void.class && fallbackClass == clazz) {
								boolean primary = primaryObj ==null || primaryObj == obj;
								Object proxy = ggxServer.registerRpcClient(interfaceClass, obj);
								// defaultListableBeanFactory.registerSingleton("userDao",userDao);
								registerRpcProxyBean(interfaceClass.getSimpleName(), interfaceClass, proxy, primary);
							} else {
								ggxServer.registerRpcService(interfaceClass, obj);
							}
						}
						break;
					}
				}

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

	public boolean checkPackage(Class<?> checkClass) {
		if (this.basicPackage != null && this.basicPackage.length > 0) {
			String className = checkClass.getName();
			for (String packa : basicPackage) {
				if (className.startsWith(packa)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	public void setBasicPackage(String[] basicPackage) {
		this.basicPackage = basicPackage;
	}

	public String[] getBasicPackage() {
		return basicPackage;
	}

	public ScanResult scanPackages(String...packages) {
		try (ScanResult scanResult = new ClassGraph()
				.enableAllInfo() // Scan classes, methods, fields, annotations
				.whitelistPackages(packages) // Scan com.xyz and subpackages (omit to scan all packages)
				.scan()) {
			return scanResult;
		}

	}

		
		
		/*
		 * GGXServer ggxServerBean = beanFactory.getBean(GGXServer.class); try
		 * (ScanResult scanResult = new ClassGraph() .enableAllInfo() // Scan classes,
		 * methods, fields, annotations .whitelistPackages("com.sgslg") // Scan com.xyz
		 * and subpackages (omit to scan all packages) .scan()) { try { ClassInfoList
		 * rpcInterfaceInfoList =
		 * scanResult.getClassesWithAnnotation(GGXRpcInterface.class.getName()); for
		 * (ClassInfo info : rpcInterfaceInfoList) { String name = info.getName();
		 * Class<?> interfaceClass = Class.forName(name); GGXRpcInterface annotation =
		 * interfaceClass.getAnnotation(GGXRpcInterface.class); Class<?> fallback =
		 * annotation.fallback(); Object primaryObj =
		 * applicationContext.getBean(interfaceClass); boolean primary = primaryObj ==
		 * obj; Class<?> fallbackClass = annotation.fallback(); Object proxy =
		 * ggxServerBean.registerRpcClient(interfaceClass, obj); //
		 * defaultListableBeanFactory.registerSingleton("userDao",userDao);
		 * registerRpcProxyBean(interfaceClass.getSimpleName(), interfaceClass, proxy,
		 * false); } } catch (ClassNotFoundException e) { e.printStackTrace(); } }
		 */




}
