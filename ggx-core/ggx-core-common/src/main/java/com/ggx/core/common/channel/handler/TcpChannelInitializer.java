package com.ggx.core.common.channel.handler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.channel.handler.common.InboundCommonHandler;
import com.ggx.core.common.channel.handler.common.OutboundCommonHandler;
import com.ggx.core.common.channel.handler.idle.IdleHandler;
import com.ggx.core.common.channel.handler.tcp.TcpInboundHandler;
import com.ggx.core.common.channel.handler.tcp.TcpOutboundHandler;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

/**
 * Tcp channel初始化处理器
 * 
 * 
 * @author zai
 * 2017-08-02
 */
public class TcpChannelInitializer extends ChannelInitializer<Channel> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TcpChannelInitializer.class);
	
	protected static final AttributeKey<String> PROTOCOL_TYPE_KEY = AttributeKey.valueOf(DefaultChannelAttributeKeys.PROTOCOL_TYPE);


	private GGXCoreConfig config;
	
	
	public TcpChannelInitializer() {
		
	}
	

	public TcpChannelInitializer(GGXCoreConfig config) {
		this.config = config;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		
	   	if (config.getIdleCheckEnabled()) {
	   		
		   	 //空闲事件触发器
		   	 ch.pipeline().addLast(new IdleStateHandler(config.getReaderIdleTime(), config.getWriterIdleTime(), config.getAllIdleTime(), TimeUnit.MILLISECONDS));
		   	 
		   	 //空闲事件处理
		   	 ch.pipeline().addLast(new IdleHandler(config));
	   	}
   	 
        ch.pipeline().addLast(new TcpInboundHandler(this.config));
        
        ch.pipeline().addLast(new InboundCommonHandler(this.config));
        
        
        ch.pipeline().addLast(new TcpOutboundHandler(this.config));
        
        ch.pipeline().addLast(new OutboundCommonHandler(this.config));
        
        ch.attr(PROTOCOL_TYPE_KEY).set(ProtocolTypeConstants.TCP);
	}
	

	public GGXCoreConfig getConfig() {
		return config;
	}
	
	public void setConfig(GGXCoreConfig config) {
		this.config = config;
	}
	

}
