package com.ggx.core.common.handler.codec.impl;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.handler.codec.DecodeHandler;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.core.common.utils.logger.PackLogger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 自定协议解析
 *  包体总长度      保留内容         指令长度      指令内容          数据体
 * +-----------+----------+-----------+-----------+------------+
 * | 4 bytes   | 2 bytes  |   1 byte  |    tag    |  data body |
 * +-----------+----------+-----------+-----------+------------+
 * @author zai
 *
 */
public class DefaultDecodeHandler implements DecodeHandler {

	/**
	 * 指令长度标识占用字节数
	 */
	public static final int ACTION_TAG_LEN = 1;
	
	/**
	 * 保留内容-字节数
	 */
	public static final int RESERVE_LEN = 2;

	/**
	 * 所有标识长度
	 */
	public static final int ALL_TAG_LEN = RESERVE_LEN + ACTION_TAG_LEN;
	
	/**
	 * 协议类型channel key
	 */
	protected static final AttributeKey<String> PROTOCOL_TYPE_KEY = AttributeKey.valueOf(DefaultChannelAttributeKeys.PROTOCOL_TYPE);
	

	private GGConfig config;
	

	public DefaultDecodeHandler() {

	}

	public DefaultDecodeHandler(GGConfig config) {
		super();
		this.config = config;
	}

	public void handle(ChannelHandlerContext ctx, ByteBuf in, String protocolType) {
		
		//读取整个包体长度
		int packLen;
		if (ProtocolTypeConstants.TCP.equals(protocolType)) {
			packLen = in.readInt();
		}else if (ProtocolTypeConstants.WEBSOCKET.equals(protocolType)) {
			packLen = in.readableBytes();
		}else {
			ctx.close();
			throw new RuntimeException("Unknow protocolType !!");
		}

		Channel channel = ctx.channel();
		GGSession session = config.getSessionFactory().getSession(channel);
		
		byte[] action = null;
		byte[] message = null;
		try {
			in.readUnsignedShort();//读取预留字节
			// 读取指令标识
			int actionLen = in.readByte();
			action = new byte[actionLen];
			in.readBytes(action);
			// 读取数据体 = 总包长 - 标识长度占用字节 - 标识体占用字节数
			int bodyLen = packLen - ALL_TAG_LEN - actionLen;
			
			if (bodyLen != 0) {

				message = new byte[bodyLen];
				// 读取数据体部分byte数组
				in.readBytes(message);

			} 
		} catch (Exception e) {
			// 解码失败，触发解码错误事件
			config.getEventManager().emitEvent(new EventData<>(session, GGEvents.Codec.DECODE_ERROR, null));
			GGLoggerUtil.getLogger(this).error("Decode Error!",e);
			channel.close();
			return;
		}
		Pack pack = new Pack(action, message);
		pack.setProtocolType(protocolType);
		
		
		pack.setChannel(channel);
		
		// 获取session
		pack.setSession(session);

		// 接收包处理
		config.getReceivePackHandler().handle(pack);
		
		if (this.config.isEnablePackLogger()) {
			pack.setChannel(channel);
			pack.setOperType(Pack.OperType.RECEIVE);
			pack.setProtocolType(protocolType);
			PackLogger packLogger = this.config.getPackLogger();
			packLogger.logPack(pack);
		}

	}

}
