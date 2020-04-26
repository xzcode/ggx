package com.ggx.core.common.control;

import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.session.GGSession;

public interface IGGContolSupport extends  ExecutorSupport{
	

	/**
	 * 断开当前连接
	 * 
	 * @author zai 2017-09-21
	 */
	default GGFuture disconnect() {
		return disconnect(null, 0);
	}
	
	/**
	 * 延迟断开当前连接
	 * 
	 * @author zai 2017-09-21
	 */
	default GGFuture  disconnect(long delayMs) {
		return disconnect(null, delayMs);
	}

	/**
	 * 断开指定用户的连接
	 * 
	 * @param userId
	 * @author zai 2017-08-19 01:12:07
	 */
	default GGFuture  disconnect(GGSession session) {
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
	default GGFuture  disconnect(GGSession session, long delayMs) {
		return session.disconnect();
	}
	
	
	
}
