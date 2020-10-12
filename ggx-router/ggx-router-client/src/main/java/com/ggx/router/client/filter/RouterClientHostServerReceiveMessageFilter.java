package com.ggx.router.client.filter;

import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.filter.ReceivePackFilter;
import com.ggx.core.common.filter.chain.FilterChain;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.controller.MessageControllerManager;
import com.ggx.core.common.serializer.factory.SerializerFactory;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.router.client.RouterClient;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.event.RouterClientEvents;


public class RouterClientHostServerReceiveMessageFilter implements ReceivePackFilter{
	
	private RouterClientConfig config;
	
	private GGXCoreServer hostServer;
	private MessageControllerManager messageControllerManager;

	private RouterClient routerClient;
	
	public RouterClientHostServerReceiveMessageFilter(RouterClientConfig config) {
		this.config = config;
		this.hostServer = config.getHostServer();
		this.messageControllerManager = this.hostServer.getMessageControllerManager();
		this.routerClient = config.getRouterClient();
	}

	@Override
	public void doFilter(Pack pack, FilterChain<Pack> filterChain) {

		String actionId = pack.getActionString(hostServer.getCharset());
		//routingServer已定义的actionid,不参与路由
		if (messageControllerManager.getMethodInfo(actionId) != null) {
			filterChain.doFilter(pack);
			return;
		}
		if (pack.getSerializeType() == null) {
			String serializerType = SerializerFactory.getSerializerType(hostServer.getSerializer());
			pack.setSerializeType(serializerType);
		}
		
		//如果匹配不到路由,交由后续过滤器处理
		routerClient.route(pack)
		.addListener(f -> {
			if (!f.isDone()) {
				//发送失败，触发消息不可达事件
				this.hostServer.emitEvent(new EventData<>(pack.getSession(), RouterClientEvents.RoutingMessage.MESSAGE_UNREACHABLE, pack));
			}
		});
	}
}
