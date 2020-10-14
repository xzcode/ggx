package com.ggx.server.spring.boot.starter.support;

import java.lang.reflect.Proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.common.annotation.GGXRpcInterface;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.server.spring.boot.starter.rpc.RpcProxyFactoryBean;
import com.ggx.util.logger.GGXLogUtil;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class GGXBeanDefinitionRegistryPostProcessor implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor{

	private ApplicationContext applicationContext;
	
	private RpcClientConfig rpcClientConfig = new RpcClientConfig();
	private RpcServerConfig rpcServerConfig = new RpcServerConfig();

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
			
		
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
					Object proxy = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass}, rpcClientConfig.getProxyInvocationHandler());
					registerRpcProxyBean(interfaceClass.getSimpleName(), interfaceClass, proxy, true);
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
	
}
