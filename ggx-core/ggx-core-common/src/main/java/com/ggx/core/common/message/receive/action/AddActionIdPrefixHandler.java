package com.ggx.core.common.message.receive.action;

/**
 * actionId前缀处理器接口
 *
 * @author zai
 * 2020-09-27 12:12:08
 */
public interface AddActionIdPrefixHandler {
	
	/**
	 * 处理前缀
	 *
	 * @param oldAction
	 * @return
	 * @author zai
	 * 2020-09-27 12:12:22
	 */
	String handle(String oldAction);

}
