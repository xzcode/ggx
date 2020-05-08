package com.ggx.router.common.constant;

/**
 * 路由默认常量
 *
 * @author zai
 * 2020-04-14 16:57:57
 */
public interface RouterConstant {
	
	/**
	 * 指令前缀
	 */
	String ACTION_ID_PREFIX = "GG.ROUTER.";
	
	/**
	 * 默认服务地址
	 */
	String DEFAULT_SERVER_HOST = "localhost";
	/**
	 * 默认服务端口
	 */
	int DEFAULT_SERVER_PORT = 9999;
	
	/**
	 * 默认转发服务端
	 */
	int DEFAULT_FORWARD_SERVER_PORT = 19999;
	
	/**
	 * 默认验证token
	 */
	String DEFAULT_AUTH_TOKEN = "aa91da26-ae36-43be-b24a-834cd1960ba3";
	
	/**
	 * 默认路由组
	 */
	String DEFAULT_ROUTER_GROUP = "default_router_group";
	
	/**
	 * 默认转发路由组
	 */
	String DEFAULT_FORWARD_ROUTER_GROUP = "default_forward_router_group";

}
