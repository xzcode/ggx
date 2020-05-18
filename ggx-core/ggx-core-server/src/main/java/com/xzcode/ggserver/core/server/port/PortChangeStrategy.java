package com.xzcode.ggserver.core.server.port;

/**
 * 端口更改策略
 *
 * @author zai
 * 2020-05-18 11:00:42
 */
public interface PortChangeStrategy {
	
	/**
	 * 随机
	 */
	String RANDOM = "random";
	
	/**
	 * 递增
	 */
	String INCREMENT = "increment";
	

}
