package com.ggx.server.spring.boot.starter.support;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ggx.core.common.filter.Filter;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.rpc.common.annotation.GGXRpcFallbackService;
import com.ggx.rpc.common.annotation.GGXRpcService;
import com.ggx.server.spring.boot.starter.annotation.GGXController;
import com.ggx.server.spring.boot.starter.annotation.GGXFilter;
import com.ggx.server.spring.boot.starter.annotation.GGXRpcServiceImpl;
import com.ggx.server.starter.GGXServer;

public class GGXAnnotationComponentScanner implements ApplicationContextAware {

	protected ApplicationContext applicationContext;
	
	@Autowired
	private GGXServer ggxserver;

	@Autowired
	private RpcClientConfig rpcClientConfig;

	@PostConstruct
	public void init() {

		// 注册控制器
		Map<String, Object> messageControllers = applicationContext.getBeansWithAnnotation(GGXController.class);
		for (Entry<String, Object> entry : messageControllers.entrySet()) {
			Object obj = entry.getValue();
			ggxserver.registerMessageController(obj);
			ggxserver.registerEventController(obj);
			ggxserver.registerSubscriberController(obj);
		}

		// 注册过滤器
		Map<String, Object> filters = applicationContext.getBeansWithAnnotation(GGXFilter.class);
		for (Entry<String, Object> entry : filters.entrySet()) {
			if (!(entry.getValue() instanceof Filter)) {
				continue;
			}
			Filter<?> obj = (Filter<?>) entry.getValue();
			GGXFilter annotation = obj.getClass().getAnnotation(GGXFilter.class);
			if (annotation != null) {
				int order = annotation.value();
				ggxserver.addFilter(obj, order);
			}
		}
		
		RpcProxyManager proxyManager = rpcClientConfig.getProxyManager();
		

		// 注册rpcservice相关
		Map<String, Object> rpcServices = applicationContext.getBeansWithAnnotation(GGXRpcService.class);

		Map<String, Object> rpcFallbackServices = applicationContext
				.getBeansWithAnnotation(GGXRpcFallbackService.class);
		Map<String, Object> rpcImplServices = applicationContext.getBeansWithAnnotation(GGXRpcServiceImpl.class);

		for (Entry<String, Object> entry : rpcServices.entrySet()) {
			Object serviceProxy = entry.getValue();
			Class<?>[] interfaces = serviceProxy.getClass().getInterfaces();
			if (interfaces == null || interfaces.length == 0) {
				continue;
			}

			Class<?> rpcServiceInterface = interfaces[0];
			
			for (Entry<String, Object> implEntry : rpcImplServices.entrySet()) {
				Object impl = implEntry.getValue();
				if (rpcServiceInterface.isAssignableFrom(impl.getClass())) {
					ggxserver.registerRpcService(rpcServiceInterface, impl);
					break;
				}
			}
			
			
			Object fallback = null;
			for (Entry<String, Object> fallbackEntry : rpcFallbackServices.entrySet()) {
				Object fall = fallbackEntry.getValue();
				if (rpcServiceInterface.isAssignableFrom(fall.getClass())) {
					fallback = fall;
					break;
				}
			}
			proxyManager.register(rpcServiceInterface, serviceProxy, fallback);

		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

}
