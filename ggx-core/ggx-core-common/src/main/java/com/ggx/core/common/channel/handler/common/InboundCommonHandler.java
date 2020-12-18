package com.ggx.core.common.channel.handler.common;

import com.ggx.core.common.config.GGXCoreConfig;

import io.netty.channel.ChannelInboundHandlerAdapter;

public class InboundCommonHandler extends ChannelInboundHandlerAdapter{
	
	protected GGXCoreConfig config;
	
	public InboundCommonHandler() {
		
	}
	
	public InboundCommonHandler(GGXCoreConfig config) {
		super();
		this.config = config;
	}
	
	


	public GGXCoreConfig getConfig() {
		return config;
	}
	
	public void setConfig(GGXCoreConfig config) {
		this.config = config;
	}

}
