package com.ggx.server.spring.boot.starter.support;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.utils.GenericClassUtil;
import com.ggx.core.common.utils.MessageActionIdUtil;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.server.spring.boot.starter.annotation.GGXEventHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageFilter;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXSubscriber;
import com.ggx.server.starter.GGXServer;

public class GGXServerSpringBootAnnotationSupport implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	private GGXServer ggxServer;
	
	//给所有扫描到的actionId加上前缀
	private String actionIdPrefix;
	
	private String[] basicPackage;
	
	
	public GGXServerSpringBootAnnotationSupport(GGXServer ggxServer) {
		this.ggxServer = ggxServer;
	}

	@PostConstruct
	public void registerComponents() {
		

		//注册消息处理器
		Map<String, Object> messageHandlers = this.applicationContext.getBeansWithAnnotation(GGXMessageHandler.class);
		for (Entry<String, Object> entry : messageHandlers.entrySet()) {
			if (!(entry.getValue() instanceof MessageHandler)) {
				continue;
			}
			MessageHandler<?> obj = (MessageHandler<?>) entry.getValue();
			
			//检查包路径
			if (!checkPackage(obj.getClass())) {
				continue;
			}
			GGXMessageHandler annotation = obj.getClass().getAnnotation(GGXMessageHandler.class);
			if (annotation != null) {
				String actionId = annotation.value();
				if (!actionId.isEmpty()) {
					this.ggxServer.onMessage(actionId, obj);
				}else {
					Class<?> genericClass = GenericClassUtil.getInterfaceGenericClass(obj.getClass());
					actionId = MessageActionIdUtil.generateClassNameDotSplitActionId(genericClass);
					this.ggxServer.onMessage(actionId, obj);
				}
			}
		}
		
		//注册事件处理器
		Map<String, Object> eventHandlers = this.applicationContext.getBeansWithAnnotation(GGXEventHandler.class);
		for (Entry<String, Object> entry : eventHandlers.entrySet()) {
			if (!(entry.getValue() instanceof EventListener)) {
				continue;
			}
			EventListener<?> obj = (EventListener<?>) entry.getValue();
			//检查包路径
			if (!checkPackage(obj.getClass())) {
				continue;
			}
			GGXEventHandler annotation = obj.getClass().getAnnotation(GGXEventHandler.class);
			if (annotation != null) {
				this.ggxServer.addEventListener(annotation.value(), obj);
			}
		}
		
		//注册过滤器
		Map<String, Object> filters = this.applicationContext.getBeansWithAnnotation(GGXMessageFilter.class);
		for (Entry<String, Object> entry : filters.entrySet()) {
			if (!(entry.getValue() instanceof Filter)) {
				continue;
			}
			Filter<?> obj = (Filter<?>) entry.getValue();
			//检查包路径
			if (!checkPackage(obj.getClass())) {
				continue;
			}
			GGXMessageFilter annotation = obj.getClass().getAnnotation(GGXMessageFilter.class);
			if (annotation != null) {
				this.ggxServer.addFilter(new FilterInfo<>(obj, annotation.value()));
			}
		}
		
		
		//注册eventbus事件订阅处理器
		Map<String, Object> subscribers = this.applicationContext.getBeansWithAnnotation(GGXSubscriber.class);
		for (Entry<String, Object> entry : subscribers.entrySet()) {
			if (!(entry.getValue() instanceof Subscriber)) {
				continue;
			}
			Subscriber obj = (Subscriber) entry.getValue();
			//检查包路径
			if (!checkPackage(obj.getClass())) {
				continue;
			}
			GGXSubscriber annotation = obj.getClass().getAnnotation(GGXSubscriber.class);
			if (annotation != null) {
				this.ggxServer.subscribe(annotation.value(), obj);
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
