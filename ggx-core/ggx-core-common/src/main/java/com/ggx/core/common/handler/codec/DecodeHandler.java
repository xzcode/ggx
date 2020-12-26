package com.ggx.core.common.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface DecodeHandler {
	
	
	void handle(ChannelHandlerContext ctx, ByteBuf in, String protocolType);
	
}
