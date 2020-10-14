package com.ggx.core.common.message.receive.controller;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.controller.model.ControllerMethodInfo;

public interface MessageControllerManager {

	/**
	 * 注册控制器
	 *
	 * @param controller
	 * @author zai
	 * 2020-10-09 18:35:54
	 */
	void register(Object controller);

	/**
	 * 调用消息处理器
	 *
	 * @param messageData
	 * @return
	 * @author zai
	 * 2020-10-09 18:36:00
	 */
	Object invoke(MessageData messageData) throws Throwable;
	
	/**
	 * 获取调用的方法信息
	 *
	 * @param actionId
	 * @return
	 * @author zai
	 * 2020-10-09 18:42:59
	 */
	ControllerMethodInfo getMethodInfo(String actionId);
	

}