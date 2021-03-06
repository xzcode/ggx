package com.ggx.core.common.message.receive.handler;

import com.ggx.core.common.message.MessageData;

/**
 * 统一消息调用接口
 * 
 * @author zai
 * 2019-01-01 22:30:41
 */
public interface ReceiveMessageHandlerInfo {
	
	/**
	 * 执行方法调用
	 * @param action
	 * @param message
	 * @throws Exception
	 * 
	 * @author zai
	 * 2019-11-24 16:42:45
	 */
	public void handle(MessageData<?> messageData) throws Exception;
	
	/**
	 * 获取请求标识
	 * @return
	 * 
	 * @author zai
	 * 2019-11-24 22:32:36
	 */
	public String getAction();
	
	/**
	 * 获取消息对应的类对象
	 * @return
	 * 
	 * @author zai
	 * 2019-11-24 22:32:44
	 */
	public Class<?> getMessageClass();

}
