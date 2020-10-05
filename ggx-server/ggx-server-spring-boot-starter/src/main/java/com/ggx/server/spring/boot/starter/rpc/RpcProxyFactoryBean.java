package com.ggx.server.spring.boot.starter.rpc;

import org.springframework.beans.factory.FactoryBean;

public class RpcProxyFactoryBean<T> implements FactoryBean<T>{
	
	private Class<?> serviceInterface;
	private Object proxy;
	
	

	public RpcProxyFactoryBean(Class<?> serviceInterface, Object proxy) {
		this.serviceInterface = serviceInterface;
		this.proxy = proxy;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getObject() throws Exception {
		return (T) proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}

}
