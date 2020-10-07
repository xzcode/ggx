package com.ggx.core.common.channel.handler.codec;

import com.ggx.core.common.message.Pack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface EncodeHandler {

	
	ByteBuf handle(ChannelHandlerContext ctx, Pack pack);
	
}
