package com.ggx.core.common.handler.common;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLogUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class InboundCommonHandler extends ChannelInboundHandlerAdapter{
	
	protected GGXCoreConfig config;
	
	public InboundCommonHandler() {
		
	}
	
	public InboundCommonHandler(GGXCoreConfig config) {
		super();
		this.config = config;
	}

	public GGXCoreConfig getConfig() {
		return config;
	}
	
	public void setConfig(GGXCoreConfig config) {
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
		if (GGXLogUtil.isDebugEnabled()) {
			GGXLogUtil.getLogger(this).debug("Channel Active:{}", channel);
		}
		GGXSession session = (GGXSession)channel.attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION)).get();
		config.getEventManager().emitEvent(new EventData<>(session, GGXCoreEvents.Connection.OPENED, null));
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		config.getSessionFactory().channelInActive(ctx.channel());
		if (GGXLogUtil.isDebugEnabled()) {
			GGXLogUtil.getLogger(this).debug("channel Inactive:{}", ctx.channel());
		}
		GGXSession session = (GGXSession)ctx.channel().attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION)).get();
		config.getEventManager().emitEvent(new EventData<>(session, GGXCoreEvents.Connection.CLOSED, null));
		super.channelInactive(ctx);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		if (GGXLogUtil.isDebugEnabled()) {
			GGXLogUtil.getLogger(this).debug("Channel Unregistered:{}", ctx.channel());
		}
		super.channelUnregistered(ctx);
	}
	
	
	
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (GGXLogUtil.isDebugEnabled()) {
			GGXLogUtil.getLogger(this).debug("User Event Triggered:{}", evt);			
		}
		super.userEventTriggered(ctx, evt);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof java.io.IOException) {
			GGXLogUtil.getLogger(this).error("Inbound ERROR! {}", cause.getMessage());
			return;
		}
		GGXLogUtil.getLogger(this).error("Inbound ERROR! ", cause);
	}
	

}
