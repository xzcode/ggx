package com.ggx.core.common.channel.handler.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.EventTask;
import com.ggx.core.common.event.GGXCoreEvents;
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
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		config.getSessionFactory().channelActive(channel);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Channel Active:{}", channel);
		}
		GGXSession session = (GGXSession)channel.attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION)).get();
		config.getTaskExecutor().submitTask(new EventTask(session, GGXCoreEvents.Connection.OPENED, null, config.getEventManager(), channel));
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		config.getSessionFactory().channelInActive(ctx.channel());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("channel Inactive:{}", ctx.channel());
		}
		config.getTaskExecutor().submitTask(new EventTask((GGXSession)ctx.channel().attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION)).get(), GGXCoreEvents.Connection.CLOSED, null, config.getEventManager()));
		super.channelInactive(ctx);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Channel Unregistered:{}", ctx.channel());
		}
		super.channelUnregistered(ctx);
	}
	
	
	
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("userEventTriggered:{}", evt);			
		}
		super.userEventTriggered(ctx, evt);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof java.io.IOException) {
			LOGGER.error("Inbound ERROR! {}", cause.getMessage());
			return;
		}
		LOGGER.error("Inbound ERROR! ", cause);
	}


	public GGXCoreConfig getConfig() {
		return config;
	}
	
	public void setConfig(GGXCoreConfig config) {
		this.config = config;
	}

}
