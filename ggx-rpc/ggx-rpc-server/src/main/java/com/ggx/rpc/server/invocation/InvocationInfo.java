package com.ggx.rpc.server.invocation;

import java.lang.reflect.Method;
import java.util.Map;

public class InvocationInfo {
	
	private String fullInterfaceName;
	
	private Object instance;
	
	private Map<String, Method> methodsCache;
	

	public String getFullInterfaceName() {
		return fullInterfaceName;
	}

	public void setFullInterfaceName(String fullInterfaceName) {
		this.fullInterfaceName = fullInterfaceName;
	}

	public Object getInstance() {
		return instance;
	}
	
	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public Map<String, Method> getMethodsCache() {
		return methodsCache;
	}

	public void setMethodsCache(Map<String, Method> methodsCache) {
		this.methodsCache = methodsCache;
	}
	
	

}
