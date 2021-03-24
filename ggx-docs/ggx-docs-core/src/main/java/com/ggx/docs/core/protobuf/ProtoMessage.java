package com.ggx.docs.core.protobuf;

import java.util.List;

public class ProtoMessage {
	
	/**
	 * 消息名称
	 */
	private String messageName;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 自定义消息类型
	 */
	private List<String> customDataTypes;
	
	
	

	public ProtoMessage() {
	}

	public ProtoMessage(String messageName, String content) {
		this.messageName = messageName;
		this.content = content;
	}
	public ProtoMessage(String messageName, String content, List<String> customDataTypes) {
		this.messageName = messageName;
		this.content = content;
		this.customDataTypes = customDataTypes;
	}

	public String getMessageName() {
		return messageName;
	}
	
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public List<String> getCustomDataTypes() {
		return customDataTypes;
	}
	
	public void setCustomDataTypes(List<String> customDataTypes) {
		this.customDataTypes = customDataTypes;
	}
	

}
