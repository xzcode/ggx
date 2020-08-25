package com.ggx.server.spring.boot.starter;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.utils.GenericClassUtil;
import com.ggx.core.common.utils.MessageActionIdUtil;
import com.ggx.server.spring.boot.starter.annotation.GGXEventHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXFilter;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageHandler;

public class GGXCoreSpringAnnotationSupport implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	private ReceiveMessageManager receiveMessageManager;
	private FilterManager filterManager;
	private EventManager eventManager;
	
	//给所有扫描到的actionId加上前缀
	private String actionIdPrefix;
	
	private String[] basicPackage;
	
	
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
			
			//检查包路径
			if (!checkPackage(obj.getClass())) {
				continue;
			}
			GGXMessageHandler annotation = obj.getClass().getAnnotation(GGXMessageHandler.class);
			if (annotation != null) {
				String actionId = annotation.value();
				if (!actionId.isEmpty()) {
					this.receiveMessageManager.onMessage(actionId, obj);
				}else {
					Class<?> genericClass = GenericClassUtil.getInterfaceGenericClass(obj.getClass());
					actionId = MessageActionIdUtil.generateClassNameDotSplitActionId(genericClass);
					this.receiveMessageManager.onMessage(actionId, obj);
				}
			}
		}
		
		//注册事件处理器
		Map<String, Object> eventHandlers = this.applicationContext.getBeansWithAnnotation(GGXEventHandler.class);
		for (Entry<String, Object> entry : eventHandlers.entrySet()) {
			EventListener<?> obj = (EventListener<?>) entry.getValue();
			//检查包路径
			if (!checkPackage(obj.getClass())) {
				continue;
			}
			GGXEventHandler annotation = obj.getClass().getAnnotation(GGXEventHandler.class);
			if (annotation != null) {
				this.eventManager.addEventListener(annotation.value(), obj);
			}
		}
		
		//注册过滤器
		Map<String, Object> filters = this.applicationContext.getBeansWithAnnotation(GGXFilter.class);
		for (Entry<String, Object> entry : filters.entrySet()) {
			Filter<?> obj = (Filter<?>) entry.getValue();
			//检查包路径
			if (!checkPackage(obj.getClass())) {
				continue;
			}
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

	public String getActionIdPrefix() {
		return actionIdPrefix;
	}

	public void setActionIdPrefix(String actionIdPrefix) {
		this.actionIdPrefix = actionIdPrefix;
	}
	
	public void setBasicPackage(String[] basicPackage) {
		this.basicPackage = basicPackage;
	}
	
	public String[] getBasicPackage() {
		return basicPackage;
	}
	

}
