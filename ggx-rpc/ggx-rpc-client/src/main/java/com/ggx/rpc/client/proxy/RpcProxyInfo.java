package com.ggx.rpc.client.proxy;

import com.ggx.rpc.common.parser.InterfaceInfo;

public class RpcProxyInfo {
	
	protected String name;
	
	protected InterfaceInfo interfaceInfo;
	
	protected Object target;
	
	protected Object proxyObj;
	
	protected Object fallbackObj;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getProxyObj() {
		return proxyObj;
	}
	public void setProxyObj(Object proxyObj) {
		this.proxyObj = proxyObj;
	}
	public Object getFallbackObj() {
		return fallbackObj;
	}
	
	public void setFallbackObj(Object fallbackObj) {
		this.fallbackObj = fallbackObj;
	}
	
	public void setInterfaceInfo(InterfaceInfo interfaceInfo) {
		this.interfaceInfo = interfaceInfo;
	}
	public InterfaceInfo getInterfaceInfo() {
		return interfaceInfo;
	}
	
	public Object getTarget() {
		return target;
	}
	
	public void setTarget(Object target) {
		this.target = target;
	}
	

}
