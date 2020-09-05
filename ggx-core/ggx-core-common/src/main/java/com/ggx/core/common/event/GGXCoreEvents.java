package com.ggx.core.common.event;

/**
 * ggx 默认事件
 * 
 * @author zai
 * 2019-10-22 15:51:51
 */
public interface GGXCoreEvents {
	
	/**
	 * 空闲状态 事件
	 * 
	 * 
	 * @author zai
	 * 2017-08-03
	 */
	interface Idle {
		
		/**
	     * 读空闲
	     */
		String READ = "GGX.IDLE.READ";
	    /**
	     * 写空闲
	     */
		String WRITE = "GGX.IDLE.WRITE";
	    /**
	     * 读与写空闲
	     */
		String ALL = "GGX.IDLE.ALL";

	}
	
	
	/**
	 * 连接状态
	 * 
	 * 
	 * @author zai
	 * 2017-09-25
	 */
	interface Connection {
		
		/**
		 * 连接打开
		 */
		String OPENED = "GGX.CONN.OPENED";
		
		/**
		 * 连接关闭
		 */
		String CLOSED = "GGX.CONN.CLOSED";

	}
	
	/**
	 * 会话相关事件
	 * 
	 * @author zai
	 * 2019-12-27 11:59:28
	 */
	interface Session {
		
		/**
		 * 会话超时
		 */
		String EXPIRED = "GGX.SESSION.EXPIRED";
		

	}

}
