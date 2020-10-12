package com.ggx.core.common.message.receive.support;

import com.ggx.core.common.message.receive.controller.MessageControllerManager;

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
	MessageControllerManager getMessageControllerManager();
	
	

	default void registerController(Object controller) {
		getMessageControllerManager().register(controller);
	}

}
