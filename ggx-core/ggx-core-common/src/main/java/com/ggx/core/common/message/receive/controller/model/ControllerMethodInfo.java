package com.ggx.core.common.message.receive.controller.model;

import java.lang.reflect.Method;

public class ControllerMethodInfo {

	private Class<?> controllerClass;
	private Object controllerObj;

	private String actionId;

	private Method method;

	private Class<?>[] paramClasses;
	
	private Class<?> messageClass;
	
	private Class<?> returnClass;
	
	private Class<?> genericGeturnClass;
	
	private boolean returnMessage;
	
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
	
	public Class<?> getMessageClass() {
		return messageClass;
	}
	
	public void setMessageClass(Class<?> messageClass) {
		this.messageClass = messageClass;
	}

	public Class<?> getReturnClass() {
		return returnClass;
	}

	public void setReturnClass(Class<?> returnClass) {
		this.returnClass = returnClass;
	}

	public boolean isReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(boolean returnClassMessage) {
		this.returnMessage = returnClassMessage;
	}
	
	public Class<?> getGenericGeturnClass() {
		return genericGeturnClass;
	}
	
	public void setGenericGeturnClass(Class<?> genericGeturnClass) {
		this.genericGeturnClass = genericGeturnClass;
	}

}
