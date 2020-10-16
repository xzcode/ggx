package com.ggx.core.common.session.id;

/**
 * 会话id生成器
 * 
 * @author zai
 * 2019-12-20 13:31:05
 */
public interface SessionIdGenerator {
	
	/**
	 * 生成sessionid
	 * 
	 * @return
	 * @author zai
	 * 2019-12-20 13:31:16
	 */
	String generateSessionId();
	
}
