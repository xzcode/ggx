package com.ggx.core.common.handler.web;

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
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * websocket 消息发送处理器
 * 
 * @author zai
 * 2018-12-29 14:01:59
 */
public class WebSocketOutboundFrameHandler extends ChannelOutboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketOutboundFrameHandler.class);
    
    private GGXCoreConfig config;
    
    public WebSocketOutboundFrameHandler() {
    	
	}
    
	public WebSocketOutboundFrameHandler(GGXCoreConfig config) {
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
		
		if(msg instanceof DefaultFullHttpResponse){
			super.write(ctx, msg, promise);
			return;
		}
			
		//调用编码处理器
		ByteBuf out = config.getEncodeHandler().handle(ctx, (Pack) msg);
		
		//分析网络流量
		if (this.config.isEnableNetFlowAnalyze()) {
			this.config.getNetFlowAnalyzer().analyzeDownFlow(out.readableBytes(), this.config.getSessionFactory().getSession(ctx.channel()));
		}
		
		ChannelFuture writeAndFlush = ctx.writeAndFlush(new BinaryWebSocketFrame(out), promise);
		writeAndFlush.addListener(f -> {
			if (!f.isSuccess()) {
				if (LOGGER.isInfoEnabled()) {
            		LOGGER.error("Write And Flush msg FAILED! ,channel: {}, active: {}, \nerror: {}", channel, channel.isActive(), f.cause());
            	}
			}
		});
		
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
