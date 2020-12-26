package com.ggx.core.common.handler.pack;

import com.ggx.core.common.message.Pack;

/**
 * 接收包处理
 * 
 * @author zai
 * 2019-12-17 09:40:15
 */
public interface ReceivePackHandler {
	
	/**
	 * 进行包处理
	 * 
	 * @param pack
	 * @author zai
	 * 2019-12-17 09:40:31
	 */
	void handle(Pack pack);
	
}
