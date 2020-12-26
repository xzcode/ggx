package com.ggx.core.common.handler.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.Pack;
import com.ggx.util.logger.GGXLogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TcpOutboundHandler extends ChannelOutboundHandlerAdapter {

	private GGXCoreConfig config;
	public TcpOutboundHandler() {
	}
	
	public TcpOutboundHandler(GGXCoreConfig config) {
		this.config = config;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		
		Channel channel = ctx.channel();
		if (!channel.isActive()) {
    		if(GGXLogUtil.isDebugEnabled()){
    			GGXLogUtil.getLogger(this).debug("\nWrite channel:{} is inActive...", channel);        		
        	}
			return;
		}
		
		if (msg instanceof ByteBuf) {
			ctx.writeAndFlush(msg, promise);
			return;
		}
		
		ByteBuf out = null;
		if (msg instanceof byte[]) {
			byte[] bytes = (byte[]) msg;
			out = channel.alloc().buffer(bytes.length);
			out.writeBytes(bytes);
			ctx.writeAndFlush(out, promise);
		}
		else {
		
			//调用编码处理器
			out = config.getEncodeHandler().handle(ctx, (Pack) msg);
			
			ChannelFuture writeFuture = ctx.writeAndFlush(out, promise);
			writeFuture.addListener(f -> {
				if (!f.isSuccess()) {
					if (GGXLogUtil.isInfoEnabled()) {
						GGXLogUtil.getLogger(this).error("Write And Flush msg FAILED! ,channel: {}, active: {}, \nerror: {}", channel, channel.isActive(), f.cause());
	            	}
				}
			});
		}
		
		//分析网络流量
		if (this.config.isEnableNetFlowAnalyze()) {
			this.config.getNetFlowAnalyzer().analyzeDownFlow(out.readableBytes(), this.config.getSessionFactory().getSession(ctx.channel()));
		}
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
