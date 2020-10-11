package com.ggx.core.common.filter;

import com.ggx.core.common.message.MessageData;

/**
 * 消息过滤器
 * 
 * @author zai
 * 2019-11-08 10:44:15
 */
public interface ReceiveMessageFilter extends MessageFilter{

	@Override
	default boolean doSendFilter(MessageData sendData) {
		return true;
	}
	
	
}
