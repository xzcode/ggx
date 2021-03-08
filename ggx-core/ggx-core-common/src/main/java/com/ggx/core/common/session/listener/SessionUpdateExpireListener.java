package com.ggx.core.common.session.listener;

import com.ggx.core.common.session.GGXSession;

/**
 * 会话更新超时时间监听器
 *
 * 2021-03-09 00:21:25
 */
public interface SessionUpdateExpireListener {
	
	/**
	 * 更新处理
	 *
	 * @param session
	 * 2021-03-09 00:21:55
	 */
	void onUpdateExpire(GGXSession session);
	
}
