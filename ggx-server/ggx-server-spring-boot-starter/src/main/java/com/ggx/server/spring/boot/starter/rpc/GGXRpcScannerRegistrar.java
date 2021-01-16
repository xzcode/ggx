package com.ggx.server.spring.boot.starter.rpc;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.ggx.rpc.common.annotation.GGXRpcService;
import com.ggx.server.spring.boot.starter.annotation.GGXRpcScan;
import com.ggx.util.logger.GGXLogUtil;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class GGXRpcScannerRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
		BeanNameGenerator importBeanNameGenerator) {
		ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry, importBeanNameGenerator);
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		try {
			// 获取所有注解的属性和值
			AnnotationAttributes annoAttrs = AnnotationAttributes
					.fromMap(importingClassMetadata.getAnnotationAttributes(GGXRpcScan.class.getName()));
			// 获取到basePackage的值
			String[] basePackages = annoAttrs.getStringArray("basePackage");
			// 如果没有设置basePackage 扫描路径,就扫描对应包下面的值
			if (basePackages.length == 0) {
				basePackages = new String[] { Class.forName(importingClassMetadata.getClassName()).getPackage().getName()};
			}
			
			// 注册RPC服务
			ClassGraph classGraph = new ClassGraph();
			classGraph.enableAllInfo();
			classGraph.acceptPackages(basePackages);

			try (ScanResult scanResult = classGraph.scan()) {

				ClassInfoList rpcInterfaceInfoList = scanResult.getClassesWithAnnotation(GGXRpcService.class.getName());
				for (ClassInfo info : rpcInterfaceInfoList) {
					Class<?> interfaceClass = info.loadClass();

					
					registerRpcProxyBean(registry, interfaceClass.getSimpleName(), interfaceClass);

				}
			} catch (Exception e) {
				GGXLogUtil.getLogger(this).error("GGXRpc Scan Error!", e);
			}
			
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("GGXRpc Scan Error!", e);
		}
	}
	

	public void registerRpcProxyBean(BeanDefinitionRegistry registry, String name, Class<?> clazz) {

		if (registry.containsBeanDefinition(name)) {
			throw new RuntimeException("Duplicate bean name!");
		}

		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);

		GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
		beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, clazz);
		beanDefinition.setBeanClass(RpcProxyFactoryBean.class);

		beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
		beanDefinition.setPrimary(true);
		beanDefinition.setAutowireCandidate(true);

		registry.registerBeanDefinition(name, beanDefinition);

	}

}
