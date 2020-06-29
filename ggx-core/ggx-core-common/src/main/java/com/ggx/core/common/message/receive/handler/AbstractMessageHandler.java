package com.ggx.core.common.message.receive.handler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;

public abstract class AbstractMessageHandler<M extends Message> implements MessageHandler<M>{
	
	protected ReceiveMessageManager receiveMessageManager;
	
	public AbstractMessageHandler(ReceiveMessageManager receiveMessageManager) {
		this.receiveMessageManager = receiveMessageManager;
		this.receiveMessageManager.onMessage(getActionId(), this);
	}

	public String getActionId() {
		try {
			//自动获取父类泛型M的消息实际类型,并获取其中定义的actionId
			ParameterizedType superType = (ParameterizedType) this.getClass().getGenericSuperclass();
			if (superType != null) {
				Type[] typeArguments = superType.getActualTypeArguments();
				if (typeArguments != null && typeArguments.length > 0) {
					for (Type type : typeArguments) {
						if (type == Message.class) {
							@SuppressWarnings("unchecked")
							Class<M> clazzM = (Class<M>) type;
							M mObj = clazzM.newInstance();
							String methodName = Message.class.getDeclaredMethods()[0].getName();
							Method method = clazzM.getDeclaredMethod(methodName);
							if (method != null) {
								String actionId = (String) method.invoke(mObj);
								return actionId;
							}
						}
					}
					
				}
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException("Get Action Id Failed!", e);
		}
	}
	
}
