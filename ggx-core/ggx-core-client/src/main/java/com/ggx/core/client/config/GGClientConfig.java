package com.ggx.core.client.config;

import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.message.pingpong.GGPingPongClientEventListener;
import com.ggx.core.common.message.pingpong.GGPongResponseHandler;
import com.ggx.core.common.message.pingpong.model.GGPong;

import io.netty.bootstrap.Bootstrap;

/**
 * 客户端配置
 * 
 * @author zai 2019-12-12 19:41:19
 */
public class GGClientConfig extends GGConfig {

	private Bootstrap bootstrap = new Bootstrap();

	private String host;

	private int port;

	@Override
	public void init() {

		super.init();

		if (isPingPongEnabled()) {
			receiveMessageManager.onMessage(GGPong.ACTION_ID, new GGPongResponseHandler(this));
			eventManager.addEventListener(GGEvents.Idle.WRITE, new GGPingPongClientEventListener(this));
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
