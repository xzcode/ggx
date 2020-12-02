package com.ggx.core.common.channel.handler.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGXSession;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class InboundCommonHandler extends ChannelInboundHandlerAdapter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InboundCommonHandler.class);
	
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
