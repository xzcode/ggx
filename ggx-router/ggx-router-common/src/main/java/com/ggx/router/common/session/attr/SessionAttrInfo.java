package com.ggx.router.common.session.attr;

/**
 * 会话属性信息
 *
 * 2021-02-21 20:14:47
 */
public class SessionAttrInfo {
	
	private String key;
	
	private Class<?> attrClass;
	
	
	
	public SessionAttrInfo() {
	}
	public SessionAttrInfo(String key, Class<?> attrClass) {
		this.key = key;
		this.attrClass = attrClass;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Class<?> getAttrClass() {
		return attrClass;
	}
	public void setAttrClass(Class<?> attrClass) {
		this.attrClass = attrClass;
	}
	
	
	
	

}
