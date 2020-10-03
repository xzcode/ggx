package com.ggx.rpc.client.proxy;

public class RpcProxyInfo {
	
	protected String name;
	protected Class<?> proxyInterfaceClazz;
	protected Object proxyObj;
	protected Class<?> fallbackClazz;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getProxyInterfaceClazz() {
		return proxyInterfaceClazz;
	}
	public void setProxyInterfaceClazz(Class<?> proxyInterfaceClazz) {
		this.proxyInterfaceClazz = proxyInterfaceClazz;
	}
	public Object getProxyObj() {
		return proxyObj;
	}
	public void setProxyObj(Object proxyObj) {
		this.proxyObj = proxyObj;
	}
	public Class<?> getFallbackClazz() {
		return fallbackClazz;
	}
	public void setFallbackClazz(Class<?> fallbackClazz) {
		this.fallbackClazz = fallbackClazz;
	}
	
	
	

}
