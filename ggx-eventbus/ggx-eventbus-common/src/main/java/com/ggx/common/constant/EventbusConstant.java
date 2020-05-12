package com.ggx.common.constant;

/**
 * eventbus默认常量
 *
 * @author zai
 * 2020-04-06 22:11:56
 */
public interface EventbusConstant {
	
	/**
	 * 指令前缀
	 */
	String ACTION_ID_PREFIX = "GG.EVENTBUS.";
	
	/**
	 * 默认服务端口
	 */
	int DEFAULT_SERVER_PORT = 16384;
	
	/**
	 * 默认验证token
	 */
	String DEFAULT_AUTH_TOKEN = "70507975-1bb7-4c7f-be43-a22e4e2540a6";
	
	
	/**
	 * 注册中心自定义事件组KEY
	 */
	String REGISTRY_CUSTOM_EVENTBUS_GROUP_KEY = "REGISTRY_CUSTOM_EVENTBUS_GROUP";
	
	/**
	 * 默认事件组
	 */
	String DEFAULT_EVENTBUS_GROUP_ID = "default_eventbus_group";
	
	
	/**
	 * 注册中心自定义数据端口KEY
	 */
	String REGISTRY_CUSTOM_EVENTBUS_PORT_KEY = "REGISTRY_CUSTOM_EVENTBUS_PORT";

}
