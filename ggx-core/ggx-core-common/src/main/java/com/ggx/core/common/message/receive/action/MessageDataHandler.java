package com.ggx.core.common.message.receive.action;


import com.ggx.core.common.message.MessageData;

/**
 * 消息绑定接口
 * 
 * @param <T>
 * @author zai
 * 2019-01-01 22:09:24
 */
public interface MessageDataHandler<T> {

	/**
	 * 处理消息
	 * @param session
	 * @param t
	 * 
	 * @author zai
	 * 2019-11-24 22:35:17
	 */
	void handle(MessageData<T> messageData);
	
}
