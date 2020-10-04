package com.ggx.server.spring.boot.starter.support;

import java.lang.reflect.AnnotatedType;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.utils.GenericClassUtil;
import com.ggx.core.common.utils.MessageActionIdUtil;
import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.rpc.common.annotation.GGXRpcInterface;
import com.ggx.server.spring.boot.starter.annotation.GGXEventHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageFilter;
import com.ggx.server.spring.boot.starter.annotation.GGXMessageHandler;
import com.ggx.server.spring.boot.starter.annotation.GGXSubscriber;
import com.ggx.server.starter.GGXServer;

public class GGXServerSpringBootAnnotationSupport implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	private GGXServer ggxServer;
	
	private String[] basicPackage;
	
	
	public GGXServerSpringBootAnnotationSupport() {
		
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
		
		
		//注册RPC服务
		
		Map<String, Object> rpcServices = this.applicationContext.getBeansWithAnnotation(GGXRpcInterface.class);
		for (Entry<String, Object> entry : rpcServices.entrySet()) {
			Object obj = entry.getValue();
			Class<? extends Object> clazz = obj.getClass();
			AnnotatedType[] annotatedInterfaces = clazz.getAnnotatedInterfaces();
			for (AnnotatedType annotatedType : annotatedInterfaces) {
				Class<?> interfaceClass = (Class<?>) annotatedType.getType();
				GGXRpcInterface annotation = interfaceClass.getDeclaredAnnotation(GGXRpcInterface.class);
				if (annotation != null) {
					Class<?> fallbackClass = annotation.fallback();

					if (fallbackClass != Void.class && fallbackClass == clazz) {
						Object proxy = this.ggxServer.registerRpcClient(interfaceClass, obj);
						registerRpcProxyBean(interfaceClass.getSimpleName(), interfaceClass, proxy);
					} else {
						this.ggxServer.registerRpcService(interfaceClass, obj);
					}
				}
				break;
			}
		}
		 
		

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}
	
	public void registerRpcProxyBean(String name, Class<?> clazz, Object obj) {
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) this.applicationContext;
        if(applicationContext.containsBean(name)) {
            throw new RuntimeException("Duplicate bean name!");
        }

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        
        GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();
        beanDefinition.setBeanClass(new FactoryBean<Object>() {

			@Override
			public Object getObject() throws Exception {
				return obj;
			}

			@Override
			public Class<?> getObjectType() {
				return clazz;
			}
			
		}.getClass());
      //这里采用的是byType方式注入，类似的还有byName等
    beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
    beanDefinition.setPrimary(true);
    BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
    registry.registerBeanDefinition(name, beanDefinition);
        
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

	
	public void setBasicPackage(String[] basicPackage) {
		this.basicPackage = basicPackage;
	}
	
	public String[] getBasicPackage() {
		return basicPackage;
	}
	

}
