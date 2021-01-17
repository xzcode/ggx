package com.ggx.rpc.common.model;

public class InterfaceInfoModel {
	
	private String interfaceName;
	
	private String crossGroup;
	
	
	public InterfaceInfoModel() {
	}
	public InterfaceInfoModel(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
	public InterfaceInfoModel(String interfaceName, String crossGroup) {
		super();
		this.interfaceName = interfaceName;
		this.crossGroup = crossGroup;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
	public String getCrossGroup() {
		return crossGroup;
	}
	
	public void setCrossGroup(String crossGroup) {
		this.crossGroup = crossGroup;
	}

}
