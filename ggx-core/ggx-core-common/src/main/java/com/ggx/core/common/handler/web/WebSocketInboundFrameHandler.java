package com.ggx.core.common.handler.web;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGXSession;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

public class WebSocketInboundFrameHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketInboundFrameHandler.class);

	private WebSocketServerHandshaker handshaker;

	private GGXCoreConfig config;

	public WebSocketInboundFrameHandler(GGXCoreConfig config) {
		this.config = config;
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {

		if (!req.decoderResult().isSuccess()) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
			return;
		}

		if (req.method() != GET) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
			return;
		}

		// Handshake
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req),
				null, true, config.getMaxDataLength());
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			try {
				handshaker.handshake(ctx.channel(), req);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				ctx.channel().close();
			}
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {

		// 分析网络流量
		if (this.config.isEnableNetFlowAnalyze()) {
			this.config.getNetFlowAnalyzer().analyzeUpFlow(frame.content().readableBytes(),
					this.config.getSessionFactory().getSession(ctx.channel()));
		}

		if (frame instanceof BinaryWebSocketFrame) {
			ByteBuf in = ((BinaryWebSocketFrame) frame).content();
			// 调用解码处理器
			config.getDecodeHandler().handle(ctx, in, ProtocolTypeConstants.WEBSOCKET);
			return;
		}
		if (frame instanceof CloseWebSocketFrame) {
			ctx.close();
			return;
		}
		if (frame instanceof TextWebSocketFrame) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("\nReceived string message:\nchannel{}\ntext:{} ; Channel Close!!", ctx.channel(),
						((TextWebSocketFrame) frame).text());
			}
			ctx.writeAndFlush("Unsupport Text Messages!").addListener(e -> {
				ctx.close();
			});
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}

		ctx.close();

	}

	private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.status().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			HttpUtil.setContentLength(res, res.content().readableBytes());
		}

		// 分析网络流量
		if (this.config.isEnableNetFlowAnalyze()) {
			this.config.getNetFlowAnalyzer().analyzeDownFlow(res.content().readableBytes(),
					this.config.getSessionFactory().getSession(ctx.channel()));
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private String getWebSocketLocation(FullHttpRequest req) {
		String location = req.headers().get(HttpHeaderNames.HOST) + this.config.getWebsocketPath();
		return "wss://" + location;
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
		GGXSession session = (GGXSession) channel.attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION)).get();
		config.getEventManager().emitEvent(new EventData<>(session, GGXCoreEvents.Connection.OPENED, null));
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		config.getSessionFactory().channelInActive(ctx.channel());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("channel Inactive:{}", ctx.channel());
		}
		GGXSession session = (GGXSession) ctx.channel().attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION))
				.get();
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