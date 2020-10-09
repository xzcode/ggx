package com.ggx.core.common.message.receive.handler;


import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.model.Message;

/**
 * 带反参的消息处理器
 * @param <T>
 * @param <E>
 * @author zai
 * 2020-10-7 21:41:19
 */
public interface ReturnableMessageHandler<T extends Message, R> {

	/**
	 * 处理消息
	 * @param session
	 * @param t
	 * 
	 * @author zai
	 * 2019-11-24 22:35:17
	 */
	R handle(MessageData<T> messageData);
	
}
