package com.ggx.server.starter;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.common.message.EventbusMessage;
import com.ggx.core.common.config.GGXCoreSupport;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGXSession;
import com.ggx.registry.client.RegistryClient;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

public interface GGXServerStarter extends GGXCoreSupport{
	
	
	/**
	 * 启动
	 *
	 * @author zai
	 * 2020-08-21 18:24:48
	 */
	void start();
	
	
	/**
	 * 关闭
	 *
	 * @author zai
	 * 2020-08-24 14:29:21
	 */
	GGXFuture<?> shutdown();
	
	
	/**
	 * 注册eventbus事件控制器
	 * @param controller
	 */
	void registerSubscriberController(Object controller);
	
	/**
	 * 发布eventbus事件
	 * @param eventId
	 * @param subscriber
	 */
	void publishEventbusMessage(EventbusMessage message);
	
	
	/**
	 * 注册RPC服务
	 * @param serviceInterface
	 * @param serviceObj
	 * @author zai
	 * 2020-10-4 21:11:40
	 */
	void registerRpcService(Class<?> serviceInterface, Object serviceObj);
	
	/**
	 * 注册RPC客户端服务
	 * @param serviceInterface
	 * @param fallbackObj
	 * @author zai
	 * 2020-10-4 21:11:49
	 */
	Object registerRpcClient(Class<?> serviceInterface, Object fallbackObj);
	
	/**
	 * 路由消息
	 * @param message
	 * @param session
	 * @author zai
	 * 2020-10-11 22:55:58
	 */
	GGXFuture<?> routeMessage(String groupId, Message message, GGXSession session);

	/**
	 * 路由消息
	 *
	 * @param groupId
	 * @param serviceId
	 * @param message
	 * @param session
	 * @return
	 * @author zai
	 * 2020-10-13 14:50:15
	 */
	GGXFuture<?> routeMessage(String groupId, String serviceId, Message message, GGXSession session);
	
	/**
	 * 注册路由会话属性传递
	 *
	 * @param key
	 * @param clazz
	 * 2021-02-21 23:08:03
	 */
	void registerRouterTransferSessionAttr(String key, Class<?> clazz);


	/**
	 * 获取注册中心客户端对象
	 *
	 * @return
	 * 2021-01-15 18:49:56
	 */
	RegistryClient getRegistryClient();


	/**
	 * 获取路由组
	 *
	 * @param serviceGroupId
	 * @return
	 * 2021-02-20 12:22:39
	 */
	RouterServiceGroup getDefaultRouterServiceGroup(String serviceGroupId);

}
