package com.ggx.admin.server.handler.services.model.resp;

public class CustomData {

	private String key;
	private String value;
	
	public CustomData() {
		
	}

	public CustomData(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
