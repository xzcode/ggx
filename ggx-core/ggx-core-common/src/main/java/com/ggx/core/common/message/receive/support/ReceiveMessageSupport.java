package com.ggx.core.common.message.receive.support;

import com.ggx.core.common.message.receive.handler.MessageHandler;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;

/**
 * 消息发送接口
 * 
 * 
 * @author zai
 * 2019-02-09 14:50:27
 */
public interface ReceiveMessageSupport {
	
	/**
	 * 获取消息请求管理器
	 * 
	 * @return
	 * @author zai
	 * 2019-12-11 14:19:44
	 */
	ReceiveMessageManager getReceiveMessageManager();
	
	

	/**
	 * 动态监听消息
	 * 
	 * @param string
	 * @param messageHandler
	 * @author zai
	 * 2019-01-02 09:41:59
	 * @param <T>
	 */
	default <T> void onMessage(String actionId, MessageHandler<?> messageHandler) {
		getReceiveMessageManager().onMessage(actionId, messageHandler);
	}
	
	default <T> void onMessage(MessageHandler<?> messageHandler) {
		getReceiveMessageManager().onMessage(messageHandler);
	}

}
