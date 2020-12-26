package com.ggx.core.common.handler.common;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.util.logger.GGXLogUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class OutboundCommonHandler extends ChannelOutboundHandlerAdapter{
	
	protected GGXCoreConfig config;
	
	public OutboundCommonHandler(GGXCoreConfig config) {
		this.config = config;
	}
	

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof java.io.IOException) {
			GGXLogUtil.getLogger(this).error("Outbound ERROR! {}", cause.getMessage());
			return;
		}
		GGXLogUtil.getLogger(this).error("Outbound ERROR! ", cause);
	}

}
