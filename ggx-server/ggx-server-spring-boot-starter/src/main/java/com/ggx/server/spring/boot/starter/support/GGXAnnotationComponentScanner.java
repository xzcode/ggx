package com.ggx.server.spring.boot.starter.support;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.filter.Filter;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.rpc.common.annotation.GGXRpcInterface;
import com.ggx.server.spring.boot.starter.annotation.GGXController;
import com.ggx.server.spring.boot.starter.annotation.GGXEventHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageFilter;
import com.ggx.server.spring.boot.starter.annotation.GGXRpcService;
import com.ggx.server.spring.boot.starter.annotation.GGXSubscriber;
import com.ggx.server.starter.GGXServer;

public class GGXAnnotationComponentScanner  implements ApplicationContextAware {

	protected ApplicationContext applicationContext;
	
	@Autowired
	private GGXServer ggxserver;
	
	@PostConstruct
	public void init() {
		// 注册消息处理器
		Map<String, Object> messageControllers = applicationContext.getBeansWithAnnotation(GGXController.class);
		for (Entry<String, Object> entry : messageControllers.entrySet()) {
			Object obj = entry.getValue();
			ggxserver.registerController(obj);
		}

		// 注册事件处理器
		Map<String, Object> eventHandlers = applicationContext.getBeansWithAnnotation(GGXEventHandler.class);
		for (Entry<String, Object> entry : eventHandlers.entrySet()) {
			if (!(entry.getValue() instanceof EventListener)) {
				continue;
			}
			EventListener<?> obj = (EventListener<?>) entry.getValue();
			GGXEventHandler annotation = obj.getClass().getAnnotation(GGXEventHandler.class);
			if (annotation != null) {
				ggxserver.addEventListener(annotation.value(), obj);
			}
		}

		// 注册过滤器
		Map<String, Object> filters = applicationContext.getBeansWithAnnotation(GGXMessageFilter.class);
		for (Entry<String, Object> entry : filters.entrySet()) {
			if (!(entry.getValue() instanceof Filter)) {
				continue;
			}
			Filter<?> obj = (Filter<?>) entry.getValue();
			GGXMessageFilter annotation = obj.getClass().getAnnotation(GGXMessageFilter.class);
			if (annotation != null) {
				int order = annotation.value();
				ggxserver.addFilter(obj, order);
			}
		}

		// 注册eventbus事件订阅处理器
		Map<String, Object> subscribers = applicationContext.getBeansWithAnnotation(GGXSubscriber.class);
		for (Entry<String, Object> entry : subscribers.entrySet()) {
			if (!(entry.getValue() instanceof Subscriber)) {
				continue;
			}
			Subscriber obj = (Subscriber) entry.getValue();
			GGXSubscriber annotation = obj.getClass().getAnnotation(GGXSubscriber.class);
			if (annotation != null) {
				ggxserver.subscribe(annotation.value(), obj);
			}
		}
		
		// 注册RpcService
		Map<String, Object> rpcServiceImpls = applicationContext.getBeansWithAnnotation(GGXRpcInterface.class);
		for (Entry<String, Object> entry : subscribers.entrySet()) {
			if (!(entry.getValue() instanceof Subscriber)) {
				continue;
			}
			Subscriber obj = (Subscriber) entry.getValue();
			GGXSubscriber annotation = obj.getClass().getAnnotation(GGXSubscriber.class);
			if (annotation != null) {
				ggxserver.subscribe(annotation.value(), obj);
			}
		}
		
		//ggxserver.registerRpcClient(serviceInterface, fallbackObj)
		//ggxserver.registerRpcService(serviceInterface, serviceObj);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
