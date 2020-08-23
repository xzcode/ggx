package com.ggx.core.common.handler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.handler.common.InboundCommonHandler;
import com.ggx.core.common.handler.common.OutboundCommonHandler;
import com.ggx.core.common.handler.exception.ExceptionHandler;
import com.ggx.core.common.handler.idle.IdleHandler;
import com.ggx.core.common.handler.web.WebSocketInboundFrameHandler;
import com.ggx.core.common.handler.web.WebSocketOutboundFrameHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

/**
 * WebSocket channel 初始化处理器
 * 
 * 
 * @author zai
 * 2017-08-02
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketChannelInitializer.class);
	
	protected static final AttributeKey<String> PROTOCOL_TYPE_KEY = AttributeKey.valueOf(DefaultChannelAttributeKeys.PROTOCOL_TYPE);
	
	private GGXCoreConfig config;
	
	public WebSocketChannelInitializer() {
	}
	
	public WebSocketChannelInitializer(GGXCoreConfig config) {
		this.config = config;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
	   	if (config.getIdleCheckEnabled()) {
	   		
		   	 //空闲事件触发器
		   	 ch.pipeline().addLast(new IdleStateHandler(config.getReaderIdleTime(), config.getWriterIdleTime(), config.getAllIdleTime(), TimeUnit.MILLISECONDS));
		   	 
		   	 //空闲事件处理
		   	 ch.pipeline().addLast(new IdleHandler(this.config));
		   	 
	   	}
	   	
	   	ch.pipeline().addLast( new HttpServerCodec());
	   	ch.pipeline().addLast(new HttpObjectAggregator(config.getMaxDataLength()));
	   	ch.pipeline().addLast(new WebSocketInboundFrameHandler(this.config));
	   	ch.pipeline().addLast(new InboundCommonHandler(this.config));
	   	
	   	
	   	ch.pipeline().addLast(new WebSocketOutboundFrameHandler(this.config ));
        ch.pipeline().addLast(new OutboundCommonHandler(this.config));
        
        ch.pipeline().addLast(new ExceptionHandler());
        
        ch.attr(PROTOCOL_TYPE_KEY).set(ProtocolTypeConstants.WEBSOCKET);
        
        
		
	}


	public GGXCoreConfig getConfig() {
		return config;
	}
	
	public void setConfig(GGXCoreConfig config) {
		this.config = config;
	}

}
