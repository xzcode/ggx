package com.ggx.core.spring.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring多实例原型对象生成器
 *
 * @author zai
 * 2020-06-19 18:33:50
 */
public class GGXCoreSpringPrototypeBeanGenerator implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	
	public <T> T generateBean(Class<T> clazz) {
		return this.applicationContext.getBean(clazz);
	}

	public <T> T generateBean(Class<T> clazz, Object...args) {
		return this.applicationContext.getBean(clazz, args);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
