package com.ggx.core.spring.support;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.spring.support.annotation.GGXEventHandler;
import com.ggx.core.spring.support.annotation.GGXFilter;
import com.ggx.core.spring.support.annotation.GGXMessageHandler;

public class GGXCoreSpringAnnotationSupport implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	private ReceiveMessageManager receiveMessageManager;
	private FilterManager filterManager;
	private EventManager eventManager;
	
	
	public GGXCoreSpringAnnotationSupport(
			ReceiveMessageManager receiveMessageManager, 
			EventManager eventManager,
			FilterManager filterManager
			) {
		this.receiveMessageManager = receiveMessageManager;
		this.filterManager = filterManager;
		this.eventManager = eventManager;
	}

	@PostConstruct
	public void registerComponents() {
		

		//注册消息处理器
		Map<String, Object> messageHandlers = this.applicationContext.getBeansWithAnnotation(GGXMessageHandler.class);
		for (Entry<String, Object> entry : messageHandlers.entrySet()) {
			MessageHandler<?> obj = (MessageHandler<?>) entry.getValue();
			GGXMessageHandler annotation = obj.getClass().getAnnotation(GGXMessageHandler.class);
			if (annotation != null) {
				this.receiveMessageManager.onMessage(annotation.value(), obj);
			}
		}
		
		//注册事件处理器
		Map<String, Object> eventHandlers = this.applicationContext.getBeansWithAnnotation(GGXEventHandler.class);
		for (Entry<String, Object> entry : eventHandlers.entrySet()) {
			EventListener<?> obj = (EventListener<?>) entry.getValue();
			GGXEventHandler annotation = obj.getClass().getAnnotation(GGXEventHandler.class);
			if (annotation != null) {
				this.eventManager.addEventListener(annotation.value(), obj);
			}
		}
		
		//注册过滤器
		Map<String, Object> filters = this.applicationContext.getBeansWithAnnotation(GGXFilter.class);
		for (Entry<String, Object> entry : filters.entrySet()) {
			Filter<?> obj = (Filter<?>) entry.getValue();
			GGXFilter annotation = obj.getClass().getAnnotation(GGXFilter.class);
			if (annotation != null) {
				this.filterManager.addFilter(new FilterInfo<>(obj, annotation.value()));
			}
		}
		
		

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
