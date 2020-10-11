package com.ggx.server.spring.boot.starter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import com.ggx.rpc.common.annotation.GGXRpcInterface;
import com.ggx.server.spring.boot.starter.rpc.RpcProxyFactoryBean;
import com.ggx.server.starter.GGXServer;
import com.ggx.util.logger.GGXLogUtil;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class ServiceBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor,ResourceLoaderAware,ApplicationContextAware{

	private ApplicationContext applicationContext;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		
	}
	
	@Autowired
	private GGXServer ggxserver;

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
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
						Object proxy = ggxserver.registerRpcClient(interfaceClass, null);
						registerRpcProxyBean(interfaceClass.getSimpleName(), interfaceClass, proxy, true);
					}else if(
							primary != null
							&& 
							primary.getClass() != fallbackClass) {
						ggxserver.registerRpcService(interfaceClass, primary);
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
}
