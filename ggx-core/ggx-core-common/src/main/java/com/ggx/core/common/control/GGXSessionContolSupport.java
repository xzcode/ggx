package com.ggx.core.common.control;

import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.session.GGXSession;

public interface GGXSessionContolSupport extends  ExecutorSupport{
	

	/**
	 * 延迟断开当前连接
	 * 
	 * @author zai 2017-09-21
	 */
	default GGXFuture  disconnect(long delayMs) {
		return disconnect(null, delayMs);
	}

	/**
	 * 断开指定用户的连接
	 * 
	 * @param userId
	 * @author zai 2017-08-19 01:12:07
	 */
	default GGXFuture  disconnect(GGXSession session) {
		return disconnect(session, 0);
	}
	
	/**
	 * 延迟断开连接
	 * 
	 * @param userId
	 * @param delayMs 延迟时间毫秒
	 * @author zai
	 * 2019-04-17 11:18:43
	 */
	default GGXFuture  disconnect(GGXSession session, long delayMs) {
		return session.disconnect();
	}
	
	
	
}
