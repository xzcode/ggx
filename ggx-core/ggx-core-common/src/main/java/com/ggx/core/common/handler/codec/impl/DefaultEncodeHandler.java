package com.ggx.core.common.handler.codec.impl;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.handler.codec.IEncodeHandler;
import com.ggx.core.common.message.Pack;
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
 * 2018-12-07 13:38:22
 */
public class DefaultEncodeHandler implements IEncodeHandler {

	
	/**
	 * 数据包长度标识 字节数
	 */
	public static final int PACKAGE_LEN = 4;
	
	/**
	 * 保留内容-字节数
	 */
	public static final int RESERVE_LEN = 2;
	
	/**
	 * 指令长度标识占用字节数
	 */
	public static final int ACTION_TAG_LEN= 1;
	
	/**
	 * 所有标识长度
	 */
	public static final int ALL_TAG_LEN = RESERVE_LEN + ACTION_TAG_LEN;
	
	
	/**
	 * 协议类型channel key
	 */
	protected static final AttributeKey<String> PROTOCOL_TYPE_KEY = AttributeKey.valueOf(DefaultChannelAttributeKeys.PROTOCOL_TYPE);
	
	private GGConfig config;
	
	public DefaultEncodeHandler() {
	}
	
	
	public DefaultEncodeHandler(GGConfig config) {
		super();
		this.config = config;
	}

	
	
	@Override
	public ByteBuf handle(ChannelHandlerContext ctx, Pack pack){
		
		Channel channel = ctx.channel();
		String protocolType = channel.attr(PROTOCOL_TYPE_KEY).get();
		
		if (this.config.isEnablePackLogger()) {
			pack.setChannel(channel);
			pack.setOperType(Pack.OperType.SEND);
			pack.setProtocolType(protocolType);
			PackLogger packLogger = this.config.getPackLogger();
			packLogger.logPack(pack);
		}
		
		
		ByteBuf out = null;
		
		byte[] tagBytes = pack.getAction();
		byte[] bodyBytes = (byte[]) pack.getMessage();
		
		int packLen = ALL_TAG_LEN;
		
		
		packLen += tagBytes.length;
		
		if (bodyBytes != null) {
			packLen += bodyBytes.length;			
		}
		
		//判断协议类型
		if (ProtocolTypeConstants.TCP.equals(protocolType)) {
			out = ctx.alloc().buffer(packLen);
			out.writeInt(packLen);
		}else {
			out = ctx.alloc().buffer(packLen);			
		}
		
		//reserve
		out.writeShort(0);
		
		//action id
		out.writeByte(tagBytes.length);
		out.writeBytes(tagBytes);
		
		//data body
		if (bodyBytes != null) {
			out.writeBytes(bodyBytes);			
		}
		
		return out;
	}

	

}
