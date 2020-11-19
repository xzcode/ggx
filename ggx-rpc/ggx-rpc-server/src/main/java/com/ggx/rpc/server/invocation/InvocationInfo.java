package com.ggx.rpc.server.invocation;

import com.ggx.rpc.common.Interfaceinfo.InterfaceInfo;

public class InvocationInfo {
	
	
	private InterfaceInfo interfaceInfo;
	
	private Object instance;
	
	public void setInterfaceInfo(InterfaceInfo interfaceInfo) {
		this.interfaceInfo = interfaceInfo;
	}
	
	public InterfaceInfo getInterfaceInfo() {
		return interfaceInfo;
	}

	public Object getInstance() {
		return instance;
	}
	
	public void setInstance(Object instance) {
		this.instance = instance;
	}


}
