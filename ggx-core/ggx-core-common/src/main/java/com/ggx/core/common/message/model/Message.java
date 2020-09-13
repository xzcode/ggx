package com.ggx.core.common.message.model;

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
	
	/**
	  * 获取actionId前缀
	 * @return
	 * @author zai
	 * 2020-9-13 13:13:41
	 */
	default String getActionIdPrefix() {
		return "";
	}
}
