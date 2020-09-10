package com.ggx.core.common.session.listener;

import com.ggx.core.common.session.GGXSession;

/**
 * 会话断开连接监听器
 * 
 * @author zai
 * 2019-12-19 17:36:05
 */
public interface SessionDisconnectListener {
	
	/**
	 * 断开处理
	 * 
	 * @param session
	 * @author zai
	 * 2019-12-19 17:36:44
	 */
	void onDisconnect(GGXSession session);
	
}
