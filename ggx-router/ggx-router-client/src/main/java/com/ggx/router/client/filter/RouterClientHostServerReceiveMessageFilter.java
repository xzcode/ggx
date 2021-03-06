package com.ggx.router.client.filter;

import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.filter.BeforeDeserializeFilter;
import com.ggx.core.common.handler.serializer.factory.SerializerFactory;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.event.RouterClientEvents;
import com.xzcode.ggserver.core.server.GGServer;


public class RouterClientHostServerReceiveMessageFilter implements BeforeDeserializeFilter{
	
	private RouterClientConfig config;
	
	private GGServer hostServer;
	private ReceiveMessageManager requestMessageManager;
	
	public RouterClientHostServerReceiveMessageFilter(RouterClientConfig config) {
		this.config = config;
		this.hostServer = config.getHostServer();
		this.requestMessageManager = this.hostServer.getReceiveMessageManager();
	}

	@Override
	public boolean doFilter(Pack pack) {

		String actionId = pack.getActionString(hostServer.getCharset());
		//routingServer已定义的actionid,不参与路由
		if (requestMessageManager.getMessageHandler(actionId) != null) {
			return true;
		}
		if (pack.getSerializeType() == null) {
			String serializerType = SerializerFactory.getSerializerType(hostServer.getSerializer());
			pack.setSerializeType(serializerType);
		}
		
		//如果匹配不到路由,交由后续过滤器处理
		config.getRouterClient().route(pack)
		.addListener(f -> {
			if (!f.isDone()) {
				//发送失败，触发消息不可达事件
				this.hostServer.emitEvent(new EventData<>(pack.getSession(), RouterClientEvents.RoutingMessage.MESSAGE_UNREACHABLE, pack));
			}
		});
		;
		return false;
	}

}
