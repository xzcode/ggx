package com.ggx.core.common.channel.handler.tcp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.channel.handler.codec.impl.DefaultDecodeHandler;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGXSession;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;

/**
 * 数据输入控制器
 * @author zai
 *
 */
public class TcpInboundHandler extends ByteToMessageDecoder{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDecodeHandler.class);

	//数据包长度标识 字节数
	public static final int PACKAGE_LEN = 4;
	
	private GGXCoreConfig config;
	
	public TcpInboundHandler(GGXCoreConfig config) {
		this.config = config;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		while (true) {
			
			int readableBytes = in.readableBytes();
			
			//如果长度不足4位，放弃并等待下次读取
			if (readableBytes < PACKAGE_LEN) {
				return;
			}
			
			in.markReaderIndex();
			int packLen = in.readInt();
			in.resetReaderIndex();
			
			//分析网络流量
			if (this.config.isEnableNetFlowAnalyze()) {
				this.config.getNetFlowAnalyzer().analyzeUpFlow(PACKAGE_LEN + packLen, this.config.getSessionFactory().getSession(ctx.channel()));
			}
			
			if (packLen > config.getMaxDataLength()) {
				config.getEventManager().emitEvent(new EventData<>(GGXCoreEvents.Codec.PACKAGE_OVERSIZE, ctx.channel()));
				LOGGER.error("Package length {} is over limit {} ! Channel close !", packLen, config.getMaxDataLength());
				ctx.close();
				return;
			}
			
			if (readableBytes - PACKAGE_LEN < packLen) {
				in.resetReaderIndex();
				return;
			}
			//调用解码处理器
			this.config.getDecodeHandler().handle(ctx, in, ProtocolTypeConstants.TCP);
		}
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
		config.getEventManager().emitEvent(new EventData<>(session, GGXCoreEvents.Connection.OPENED, null));
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		config.getSessionFactory().channelInActive(ctx.channel());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("channel Inactive:{}", ctx.channel());
		}
		GGXSession session = (GGXSession)ctx.channel().attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION)).get();
		config.getEventManager().emitEvent(new EventData<>(session, GGXCoreEvents.Connection.CLOSED, null));
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
	
}