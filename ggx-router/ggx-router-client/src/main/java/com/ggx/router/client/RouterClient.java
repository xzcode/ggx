package com.ggx.router.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGXSession;
import com.ggx.router.client.config.RouterClientConfig;
import com.ggx.router.client.service.RouterServiceProvider;
import com.ggx.router.client.service.loadblancer.constant.RouterServiceProviderType;
import com.ggx.router.client.service.manager.RouterServiceManager;
import com.ggx.router.client.service.manager.group.RouterServiceGroup;

/**
 * 路由客户端
 * 
 * 
 * @author zai
 * 2019-10-03 14:04:16
 */
public class RouterClient{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RouterClient.class);
	
	private RouterClientConfig config;
	
	public RouterClient(RouterClientConfig config) {
		this.config = config;
		this.config.setRouterClient(this);
		this.config.init();
	}
	
	public GGXFuture<?> route(Pack pack) {
		try {
				return config.getServiceProvider().dispatch(pack);
		} catch (Exception e) {
			LOGGER.error("Route Message Error!", e);
		}
		return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
	}
	
	public GGXFuture<?> route(String groupId, String serviceId, Message message, GGXSession session) {
		String serviceProviderType = this.config.getServiceProviderType();
        RouterServiceGroup serviceGroup = null;
        RouterServiceProvider serviceProvider = this.config.getServiceProvider();
        if (serviceProviderType.contentEquals(RouterServiceProviderType.REGISTRY_SINGLE_SERVICE)) {
        	serviceGroup = serviceProvider.getDefaultRouterServiceGroup();
		}else {
			if (groupId == null) {
				return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
			}
			RouterServiceManager routerServiceManager = this.config.getRouterServiceManager();
			serviceGroup = routerServiceManager.getServiceGroup(groupId);
			
		}
        if (serviceGroup == null) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		Pack pack =  session.makePack(new MessageData(session, message));
		return serviceGroup.dispatch(pack, serviceId);
		
	}

	public RouterClientConfig getConfig() {
		return config;
	}
	
	public void shutdown() {
		this.config.getSharedEventLoopGroup().shutdownGracefully();
	}

}
