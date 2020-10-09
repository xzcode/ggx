package com.ggx.core.common.message.receive.controller;

import java.lang.reflect.Method;

public class ControllerMethodInfo {

	private Class<?> controllerClass;
	private Object controllerObj;

	private String actionId;

	private Method method;

	private Class<?>[] paramClasses;
	
	private int messageParamIndex = -1;
	
	private int sessionParamIndex = -1;

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Class<?>[] getParamClasses() {
		return paramClasses;
	}

	public void setParamClasses(Class<?>[] paramClasses) {
		this.paramClasses = paramClasses;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public void setControllerClass(Class<?> controllerClass) {
		this.controllerClass = controllerClass;
	}

	public Object getControllerObj() {
		return controllerObj;
	}

	public void setControllerObj(Object controllerObj) {
		this.controllerObj = controllerObj;
	}

	public int getMessageParamIndex() {
		return messageParamIndex;
	}

	public void setMessageParamIndex(int messageParamIndex) {
		this.messageParamIndex = messageParamIndex;
	}

	public int getSessionParamIndex() {
		return sessionParamIndex;
	}

	public void setSessionParamIndex(int sessionParamIndex) {
		this.sessionParamIndex = sessionParamIndex;
	}
	
	

}
