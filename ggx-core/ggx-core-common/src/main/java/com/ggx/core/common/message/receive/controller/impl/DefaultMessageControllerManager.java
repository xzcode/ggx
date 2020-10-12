package com.ggx.core.common.message.receive.controller.impl;

import java.lang.reflect.Method;
import java.util.List;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.actionid.ActionIdCacheManager;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.receive.controller.MessageControllerManager;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.message.receive.controller.model.ControllerMethodInfo;
import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.manager.impl.ListenableMapDataManager;
import com.ggx.util.reflect.GGXReflectUtil;

public class DefaultMessageControllerManager extends ListenableMapDataManager<String, ControllerMethodInfo> implements MessageControllerManager{
	
	private GGXCoreConfig config;
	
	
	public DefaultMessageControllerManager(GGXCoreConfig config) {
		this.config = config;
	}

	@Override
	public void register(Object controller) {
		
		ActionIdCacheManager actionIdCacheManager = config.getActionIdCacheManager();
		Class<? extends Object> controllerClass = controller.getClass();
		List<Method> methods = GGXReflectUtil.getAllDeclaredMethods(controllerClass);
		for (Method method : methods) {
			ControllerMethodInfo methodInfo = new ControllerMethodInfo();
			GGXAction annotation = method.getAnnotation(GGXAction.class);
			if (annotation == null) {
				continue;
			}
			Class<?> onMessageClass = annotation.onMessage();
			if (onMessageClass == Void.class) {
				onMessageClass = annotation.value();
			}
			if (onMessageClass == Void.class) {
				onMessageClass = null;
			}
			
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (onMessageClass == null) {
				int i = 0;
				for (Class<?> paramType : parameterTypes) {
					if (Message.class.isAssignableFrom(paramType)) {
						onMessageClass = paramType;
						methodInfo.setMessageParamIndex(i);
					}
					if (GGXSession.class.isAssignableFrom(paramType)) {
						methodInfo.setSessionParamIndex(i);
					}
					i++;
				}
			}
			
			if (onMessageClass != null) {
				Class<?> returnType = method.getReturnType();
				methodInfo.setReturnClass(returnType);
				methodInfo.setReturnMessage(returnType.isAssignableFrom(Message.class));
				methodInfo.setMessageClass(onMessageClass);
				methodInfo.setMethod(method);
				methodInfo.setControllerClass(controllerClass);
				methodInfo.setControllerObj(controller);
				methodInfo.setActionId(actionIdCacheManager.get(onMessageClass));
				methodInfo.setParamClasses(parameterTypes);
				
				this.put(methodInfo.getActionId(), methodInfo);
			}
			
		}
	}
	
	@Override
	public Object invoke(MessageData messageData) {
		GGXSession session = messageData.getSession();
		Message message = (Message) messageData.getMessage();
		String action = messageData.getAction();
		ControllerMethodInfo methodInfo = this.get(action);
		if (methodInfo == null) {
			GGXLogUtil.getLogger(this).error("There is no such action named '{}' !", action);
			return null;
		}
		Method method = methodInfo.getMethod();
		Object controllerObj = methodInfo.getControllerObj();
		
		if (controllerObj == null) {
			GGXLogUtil.getLogger(this).error("Handling action '{}' failed, 'controllerObj' not set!", action);
			return null;
		}
		Class<?>[] parameterTypes = method.getParameterTypes();
		
		Object returnObj = null;
		try {
			if (parameterTypes != null && parameterTypes.length > 0) {
				Object[] params = new Object[parameterTypes.length];
				if (methodInfo.getMessageParamIndex() != -1) {
					params[methodInfo.getMessageParamIndex()] = message;
				}
				if (methodInfo.getSessionParamIndex() != -1) {
					params[methodInfo.getSessionParamIndex()] = session;
				}
				returnObj = method.invoke(methodInfo.getControllerObj(),params);
			} else {
				returnObj = method.invoke(methodInfo.getControllerObj());
			} 
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Handling action '{}' ERRER!", action, e);
		}
		return returnObj;
	}

	@Override
	public ControllerMethodInfo getMethodInfo(String actionId) {
		return get(actionId);
	}
	
}