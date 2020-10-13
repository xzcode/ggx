package com.ggx.core.common.channel.handler.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.config.GGXCoreConfig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class OutboundCommonHandler extends ChannelOutboundHandlerAdapter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OutboundCommonHandler.class);
	
	protected GGXCoreConfig config;
	
	public OutboundCommonHandler(GGXCoreConfig config) {
		this.config = config;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof java.io.IOException) {
			LOGGER.error("Outbound ERROR! {}", cause.getMessage());
			return;
		}
		LOGGER.error("Outbound ERROR! ", cause);
	}

}
