package com.ggx.eventbus.group.client.config;

import com.ggx.registry.client.RegistryClient;

import io.netty.channel.EventLoopGroup;

/**
 * EventbusClient配置
 *
 * @author zai
 * 2020-04-10 11:58:47
 */
public class EventbusClientGroupConfig {

	//注册中心客户端
	protected RegistryClient registryClient;
	
	//共享线程组
	protected EventLoopGroup sharedEventLoopGroup;
	
	

}
