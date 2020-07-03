package com.ggx.core.common.message.receive.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;

/**
 * 请求消息调用模型
 * 
 * @author zai
 * 2019-01-01 22:11:15
 * @param <T>
 */
public class DefaultReceiveMessagerHandlerInfo implements ReceiveMessageHandlerInfo{
	
	
	/**
	 * 请求标识
	 */
	private String actionId;
	
	/**
	 * 接收消息的class类型
	 */
	private Class<?> messageClass;
	
	
	/**
	 * 消息调用对象
	 */
	private MessageHandler<Object> messageAcion;



	@SuppressWarnings("unchecked")
	public void handle(MessageData<?> request) throws Exception {
		messageAcion.handle((MessageData<Object>) request);
	}


	public String getActionId() {
		return actionId;
	}


	public void setActionId(String actionId) {
		this.actionId = actionId;
	}


	public Class<?> getMessageClass() {
		return messageClass;
	}


	public void setMessageClass(Class<?> messageClass) {
		this.messageClass = messageClass;
	}


	public MessageHandler<?> getHandler() {
		return messageAcion;
	}


	@SuppressWarnings("unchecked")
	public void setHandler(MessageHandler<?> messageAcion) {
		this.messageAcion =  (MessageHandler<Object>) messageAcion;
	}


}
