package com.ggx.core.common.event.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.controller.EventControllerMethodInfo;
import com.ggx.core.common.event.model.EventData;

public class ControllerMethodEventListener<T> implements EventListener<T>{
	
	private EventControllerMethodInfo methodInfo;

	public ControllerMethodEventListener(EventControllerMethodInfo methodInfo) {
		this.methodInfo = methodInfo;
	}

	@Override
	public void onEvent(EventData<T> eventData) throws Throwable{
		Method method = this.methodInfo.getMethod();
		Class<?>[] parameterTypes = this.methodInfo.getParamClasses();
		try {
			if (parameterTypes != null && parameterTypes.length > 0) {
				Object[] params = new Object[parameterTypes.length];
				if (methodInfo.getMessageParamIndex() != -1) {
					params[methodInfo.getMessageParamIndex()] = eventData;
				}
				method.invoke(methodInfo.getControllerObj(), params);
			} else {
				method.invoke(methodInfo.getControllerObj());
			} 
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}
