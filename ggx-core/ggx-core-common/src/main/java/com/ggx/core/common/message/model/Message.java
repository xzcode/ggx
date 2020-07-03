package com.ggx.core.common.message.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息模型
 * 
 * @author zai
 * 2019-12-25 11:51:14
 */
public interface Message {
	
	/**
	 * 获取actionId
	 * 
	 * @return
	 * @author zai
	 * 2019-12-25 11:52:23
	 */
	String getActionId();
}
