package com.ggx.server.spring.boot.starter.rpc;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.invocation.handler.DefaultProxyInvocationHandler;

public class RpcProxyFactoryBean <T> implements FactoryBean<T> {

	private Class<?> serviceInterface;

	@Autowired
	private RpcClientConfig rpcClientConfig;

	public RpcProxyFactoryBean(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getObject() throws Exception {
		Object proxyObj = Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[] { serviceInterface },
				new DefaultProxyInvocationHandler(rpcClientConfig, serviceInterface));
		return (T) proxyObj;
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}

}
