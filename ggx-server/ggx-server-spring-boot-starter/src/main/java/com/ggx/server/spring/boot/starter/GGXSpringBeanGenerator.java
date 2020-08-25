package com.ggx.server.spring.boot.starter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring多实例原型对象生成器
 *
 * @author zai
 * 2020-06-19 18:33:50
 */
public class GGXSpringBeanGenerator implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	

	public <T> T generateBean(Class<T> clazz) {
		return this.applicationContext.getBean(clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
