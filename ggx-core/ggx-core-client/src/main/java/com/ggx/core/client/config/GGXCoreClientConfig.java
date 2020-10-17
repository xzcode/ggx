package com.ggx.core.client.config;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.message.pingpong.GGXPingPongClientEventListener;
import com.ggx.core.common.message.pingpong.GGXPingPongController;

import io.netty.bootstrap.Bootstrap;

/**
 * 客户端配置
 * 
 * @author zai 2019-12-12 19:41:19
 */
public class GGXCoreClientConfig extends GGXCoreConfig {

	private Bootstrap bootstrap = new Bootstrap();

	private String host;

	private int port;

	@Override
	public void init() {
		setProtocolType(ProtocolTypeConstants.TCP);
		super.init();

		if (isPingPongEnabled()) {
			messageControllerManager.registerMessageController(new GGXPingPongController(this));
			eventManager.addEventListener(GGXCoreEvents.Idle.WRITE, new GGXPingPongClientEventListener(this));
		}

	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
