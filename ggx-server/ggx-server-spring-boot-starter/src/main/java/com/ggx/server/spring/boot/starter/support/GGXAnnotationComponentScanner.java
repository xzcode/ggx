package com.ggx.server.spring.boot.starter.support;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ggx.core.common.filter.Filter;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.proxy.RpcProxyInfo;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.server.spring.boot.starter.annotation.GGXController;
import com.ggx.server.spring.boot.starter.annotation.GGXFilter;
import com.ggx.server.spring.boot.starter.support.model.RpcServiceScanInfo;
import com.ggx.server.starter.GGXServer;

public class GGXAnnotationComponentScanner  implements ApplicationContextAware {

	protected ApplicationContext applicationContext;
	
	@Autowired
	private GGXServer ggxserver;
	@Autowired
	private GGXBeanDefinitionRegistryPostProcessor ggxBeanDefinitionRegistryPostProcessor;
	
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
		
		//注册rpcservice相关
		List<RpcServiceScanInfo> rpcServiceScanInfos = ggxBeanDefinitionRegistryPostProcessor.getRpcServiceScanInfos();
		
		for (RpcServiceScanInfo rpcServiceScanInfo : rpcServiceScanInfos) {
			Class<?> implClass = rpcServiceScanInfo.getImplClass();
			Class<?> interfaceClass = rpcServiceScanInfo.getInterfaceClass();
			Class<?> fallbackClass = rpcServiceScanInfo.getFallbackClass();
			Object proxyObj = rpcServiceScanInfo.getProxyObj();
			Object impl = null;
			Object fallback = null;
			if (implClass != null) {
				try {
					impl = applicationContext.getBean(implClass);
					ggxserver.registerRpcService(interfaceClass, impl);
				} catch (Exception e) {
					
				}
			}
			if (fallbackClass != null) {
				try {
					fallback = applicationContext.getBean(fallbackClass);
					
				} catch (Exception e) {
					
				}
			}
			RpcClientConfig rpcClientConfig = ggxserver.getConfig().getRpc().getClient();
			RpcProxyManager proxyManager = rpcClientConfig.getProxyManager();
			proxyManager.register(interfaceClass, proxyObj, fallback);
			RpcProxyInfo rpcProxyInfo = proxyManager.get(interfaceClass);
			rpcProxyInfo.setTarget(impl);
		}
		
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
