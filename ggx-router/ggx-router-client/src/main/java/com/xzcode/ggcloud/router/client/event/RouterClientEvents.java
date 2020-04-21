package com.xzcode.ggcloud.router.client.event;

/**
 * 路由客户端事件
 * 
 * @author zai
 * 2020-01-10 18:16:33
 */
public interface RouterClientEvents {
	
	
	/**
	 * 连接状态
	 * 
	 * 
	 * @author zai
	 * 2017-09-25
	 */
	interface RouterService {
		
		/**
		 * 服务不可用
		 */
		String UNAVAILABLE = "GG.ROUTER.CLIENT.UNAVAILABLE";
		
		/**
		 * 服务已关闭
		 */
		String SHUTDOWN = "GG.ROUTER.CLIENT.SHUTDOWN";

	}
	
	/**
	 * 消息路由事件
	 * 
	 * @author zai
	 * 2020-01-10 18:29:28
	 */
	interface RoutingMessage {
		/**
		 * 服务不可用
		 */
		String SERVICE_UNAVAILABLE = "GG.ROUTING.MESSAGE.SERVICE.UNAVAILABLE";
		
		/**
		 * 服务已关闭
		 */
		String SERVICE_SHUTDOWN = "GG.ROUTING.MESSAGE.SERVICE.SHUTDOWN";
		
		/**
		 * 消息不可达
		 */
		String MESSAGE_UNREACHABLE = "GG.ROUTING.MESSAGE.UNREACHABLE";
		
	}
	

}
