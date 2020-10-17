package com.ggx.eventbus.client.subscriber.model;

import java.lang.reflect.Method;

public class ControllerSubscriberMethodInfo {

	private Class<?> controllerClass;
	
	private Object controllerObj;

	private String eventId;

	private Method method;

	private Class<?>[] paramClasses;
	
	private Class<?> dataType;
	
	private int messageParamIndex = -1;
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String actionId) {
		this.eventId = actionId;
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

	public Class<?> getDataType() {
		return dataType;
	}
	
	public void setDataType(Class<?> messageClass) {
		this.dataType = messageClass;
	}

	
}
