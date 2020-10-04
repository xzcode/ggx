package com.ggx.rpc.common.model;

public class InterfaceInfoModel {
	
	private String interfaceName;
	private String fallbackClassName;
	
	
	public InterfaceInfoModel() {
	}
	public InterfaceInfoModel(String interfaceName, String fallbackClassName) {
		this.interfaceName = interfaceName;
		this.fallbackClassName = fallbackClassName;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getFallbackClassName() {
		return fallbackClassName;
	}
	public void setFallbackClassName(String fallbackClassName) {
		this.fallbackClassName = fallbackClassName;
	}
	
	

}
