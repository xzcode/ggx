package com.ggx.core.common.channel.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.config.GGXCoreConfig;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 混合tcp与websocket 初始化处理器
 * 
 * @author zai
 * 2019-06-15 14:31:58
 */
public class MixedSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final Logger logger = LoggerFactory.getLogger(MixedSocketChannelInitializer.class);
	
	private GGXCoreConfig config;
	
	public MixedSocketChannelInitializer() {
	}
	
	public MixedSocketChannelInitializer(GGXCoreConfig config) {
		this.config = config;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
	   	ch.pipeline().addLast(new SocketSelectHandler(config));
	}


	public GGXCoreConfig getConfig() {
		return config;
	}
	
	public void setConfig(GGXCoreConfig config) {
		this.config = config;
	}

}
