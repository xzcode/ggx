package com.ggx.eventbus.client.subscriber.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ggx.eventbus.client.subscriber.Subscriber;
import com.ggx.eventbus.client.subscriber.model.ControllerSubscriberMethodInfo;
import com.ggx.eventbus.client.subscriber.model.SubscriptionData;

public class ControllerMethodSubscriber implements Subscriber {
	
	private ControllerSubscriberMethodInfo methodInfo;

	public ControllerMethodSubscriber(ControllerSubscriberMethodInfo methodInfo) {
		this.methodInfo = methodInfo;
	}

	@Override
	public void trigger(SubscriptionData subData) throws Throwable{
		Method method = this.methodInfo.getMethod();
		Class<?>[] parameterTypes = this.methodInfo.getParamClasses();
		try {
			if (parameterTypes != null && parameterTypes.length > 0) {
				Object[] params = new Object[parameterTypes.length];
				if (methodInfo.getMessageParamIndex() != -1) {
					params[methodInfo.getMessageParamIndex()] = subData.getData();
				}
				method.invoke(methodInfo.getControllerObj(), params);
			} else {
				method.invoke(methodInfo.getControllerObj());
			} 
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Override
	public Class<?> getDataType() {
		return this.methodInfo.getDataType();
	}

}
