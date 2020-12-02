package com.ggx.core.common.channel.handler.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.Pack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 *  
 * @author Administrator
 *
 */
public class TcpOutboundHandler extends ChannelOutboundHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TcpOutboundHandler.class);
	    

	private GGXCoreConfig config;
	/*
	private static final Gson GSON = new GsonBuilder()
    		.serializeNulls()
    		.create();
	*/
	
	public TcpOutboundHandler() {
	}
	
	
	public TcpOutboundHandler(GGXCoreConfig config) {
		super();
		this.config = config;
	}

	
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		
		Channel channel = ctx.channel();
		if (!channel.isActive()) {
    		if(LOGGER.isDebugEnabled()){
        		LOGGER.debug("\nWrite channel:{} is inActive...", ctx.channel());        		
        	}
			return;
		}
		
		if (msg instanceof ByteBuf) {
			ctx.writeAndFlush(msg);
			return;
		}
		
		ByteBuf out = null;
		if (msg instanceof byte[]) {
			byte[] bytes = (byte[]) msg;
			out = ctx.alloc().buffer(bytes.length);
			out.writeBytes(bytes);
			ctx.writeAndFlush(out);
		}
		else {
		
			//调用编码处理器
			out = config.getEncodeHandler().handle(ctx, (Pack) msg);
			
			ChannelFuture writeFuture = ctx.writeAndFlush(out, promise);
			writeFuture.addListener(f -> {
				if (!f.isSuccess()) {
					if (LOGGER.isInfoEnabled()) {
	            		LOGGER.error("Write And Flush msg FAILED! ,channel: {}, active: {}, \nerror: {}", channel, channel.isActive(), f.cause());
	            	}
				}
			});
			
		}
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
