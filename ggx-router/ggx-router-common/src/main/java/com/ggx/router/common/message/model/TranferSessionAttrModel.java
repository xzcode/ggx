package com.ggx.router.common.message.model;

/**
 * 会话属性传递模型
 *
 * 2021-02-21 18:47:51
 */
public class TranferSessionAttrModel {

	private String key;

	private byte[] data;

	private String className;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
