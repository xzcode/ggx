package com.ggx.server.spring.boot.starter.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * spring多实例原型对象生成器
 *
 * @author zai
 * 2020-06-19 18:33:50
 */
public class GGXSpringBeanGenerator implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	/**
	 * 生成原型bean
	 *
	 * @param <T>
	 * @param clazz
	 * @param args
	 * @return
	 * @author zai
	 * 2020-10-16 16:15:35
	 */
	public <T> T generatePrototypeBean(Class<T> clazz, Object...args) {
		if (!this.applicationContext.containsBean(clazz.getSimpleName())) {
			registerPrototypeBean(clazz);
		}
		return this.applicationContext.getBean(clazz, args);
	}
	
	/**
	 * 注册原型bean
	 *
	 * @param clazz
	 * @author zai
	 * 2020-10-16 16:29:52
	 */
	public void registerPrototypeBean(Class<?> clazz) {
		
		synchronized (clazz) {
			
			ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) this.applicationContext;
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
	        
			GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
			
			beanDefinition.setBeanClass(clazz);
			beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
			registry.registerBeanDefinition(clazz.getSimpleName(), beanDefinition);
			
		}
		
	}
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
