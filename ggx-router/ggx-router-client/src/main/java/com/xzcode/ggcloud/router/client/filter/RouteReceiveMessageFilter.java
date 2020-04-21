package com.xzcode.ggcloud.router.client.filter;

import com.xzcode.ggcloud.router.client.config.RouterClientConfig;
import com.xzcode.ggcloud.router.client.event.RouterClientEvents;
import com.xzcode.ggserver.core.common.event.model.EventData;
import com.xzcode.ggserver.core.common.filter.BeforeDeserializeFilter;
import com.xzcode.ggserver.core.common.message.Pack;
import com.xzcode.ggserver.core.common.message.request.manager.ReceiveMessageManager;
import com.xzcode.ggserver.core.server.GGServer;

/**
 * 路由消息过滤器
 * 
 * @author zai
 * 2019-12-18 18:59:37
 */
public class RouteReceiveMessageFilter implements BeforeDeserializeFilter{
	
	private RouterClientConfig config;
	
	private GGServer routingServer;
	private ReceiveMessageManager requestMessageManager;
	
	public RouteReceiveMessageFilter(RouterClientConfig config) {
		this.config = config;
		this.routingServer = config.getRoutingServer();
		this.requestMessageManager = this.routingServer.getRequestMessageManager();
	}

	@Override
	public boolean doFilter(Pack pack) {

		String actionId = pack.getActionString(routingServer.getCharset());
		//routingServer已定义的actionid,不参与路由
		if (requestMessageManager.getMessageHandler(actionId) != null) {
			return true;
		}
		//如果匹配不到路由,交由后续过滤器处理
		config.getRouterClient().route(pack)
		.addListener(f -> {
			if (!f.isDone()) {
				//发送失败，触发消息不可达事件
				this.routingServer.emitEvent(new EventData<>(pack.getSession(), RouterClientEvents.RoutingMessage.MESSAGE_UNREACHABLE, pack));
			}
		});
		;
		return false;
	}

}
