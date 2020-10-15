package com.ggx.server.spring.boot.starter.support.model;

public class RpcServiceScanInfo {
	private Class<?> interfaceClass;
	private Class<?> implClass;
	private Class<?> fallbackClass;
	private Object proxyObj;
	
	
	public RpcServiceScanInfo(Class<?> interfaceClass, Class<?> implClass, Class<?> fallbackClass, Object proxyObj) {
		super();
		this.interfaceClass = interfaceClass;
		this.implClass = implClass;
		this.fallbackClass = fallbackClass;
		this.proxyObj = proxyObj;
	}
	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}
	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	public Class<?> getImplClass() {
		return implClass;
	}
	public void setImplClass(Class<?> implClass) {
		this.implClass = implClass;
	}
	public Class<?> getFallbackClass() {
		return fallbackClass;
	}
	public void setFallbackClass(Class<?> fallbackClass) {
		this.fallbackClass = fallbackClass;
	}
	public Object getProxyObj() {
		return proxyObj;
	}
	
	public void setProxyObj(Object proxyObj) {
		this.proxyObj = proxyObj;
	}
	

}
