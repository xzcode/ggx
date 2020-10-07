package com.ggx.core.common.message.receive.handler;


import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.model.Message;

/**
 * 消息绑定接口
 * 
 * @param <T>
 * @author zai
 * 2019-01-01 22:09:24
 */
public interface MessageHandler<T extends Message> {

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
