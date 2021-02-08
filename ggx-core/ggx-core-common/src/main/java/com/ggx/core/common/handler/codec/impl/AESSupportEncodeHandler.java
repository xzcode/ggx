package com.ggx.core.common.handler.codec.impl;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.encryption.aes.AESCipher;
import com.ggx.core.common.handler.codec.EncodeHandler;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.utils.ByteArrayTransferUtil;
import com.ggx.core.common.utils.logger.PackLogger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 自定协议解析
 *      包体总长度          指令长度       指令内容            数据体
 * +-----------+----------+-----------+------------+
 * | 4 bytes   |  1 byte  |    tag    |  data body |
 * +-----------+----------+-----------+------------+
 *             | ____________ AES CONTENT _________|
 * @author zai
 * 2018-12-07 13:38:22
 */
public class AESSupportEncodeHandler implements EncodeHandler {

	
	/**
	 * 数据包长度标识 字节数
	 */
	public static final int PACKAGE_LEN = 4;
	
	/**
	 * 指令长度标识占用字节数
	 */
	public static final int ACTION_TAG_LEN= 1;
	
	/**
	 * 所有标识长度
	 */
	public static final int ALL_TAG_LEN = ACTION_TAG_LEN;
	
	
	/**
	 * 协议类型channel key
	 */
	protected static final AttributeKey<String> PROTOCOL_TYPE_KEY = AttributeKey.valueOf(DefaultChannelAttributeKeys.PROTOCOL_TYPE);
	
	private GGXCoreConfig config;
	
	public AESSupportEncodeHandler() {
	}
	
	
	public AESSupportEncodeHandler(GGXCoreConfig config) {
		super();
		this.config = config;
	}

	
	
	@Override
	public ByteBuf handle(ChannelHandlerContext ctx, Pack pack){
		
		Channel channel = ctx.channel();
		String protocolType = channel.attr(PROTOCOL_TYPE_KEY).get();
		
		
		
		
		ByteBuf out = null;
		
		byte[] tagBytes = pack.getAction();
		byte[] bodyBytes = (byte[]) pack.getMessage();
		
		int packLen = ALL_TAG_LEN;
		
		
		packLen += tagBytes.length;
		
		if (bodyBytes != null) {
			packLen += bodyBytes.length;			
		}
		
		byte[] buff = new byte[packLen];
		int buffWriteIndex = 0;
		
		//填充 指令长度标识到缓冲区
		buff[buffWriteIndex] = (byte) tagBytes.length;
		buffWriteIndex += 1;
		
		//填充 指令内容 到缓冲区
		ByteArrayTransferUtil.fillBytes(tagBytes, buffWriteIndex, buff);
		buffWriteIndex += tagBytes.length;
		
		//填充 数据体 到缓冲区
		if (bodyBytes != null) {
			ByteArrayTransferUtil.fillBytes(bodyBytes, buffWriteIndex, buff);
			//buffWriteIndex += bodyBytes.length;
		}
		
		//进行AES加密
		AESCipher aesCipher = this.config.getAesCipher();
		buff = aesCipher.encrypt(buff);
		
		//更新包长度
		packLen = buff.length;
		
		
		//判断协议类型
		if (ProtocolTypeConstants.TCP.equals(protocolType)) {
			out = ctx.alloc().buffer(packLen);
			out.writeInt(packLen);
		}else {
			out = ctx.alloc().buffer(packLen);			
		}
		
		
		
		//数据写入netty缓冲区
		out.writeBytes(buff);
		
		if (this.config.isEnablePackLogger()) {
			pack.setChannel(channel);
			pack.setOperType(Pack.OperType.SEND);
			pack.setProtocolType(protocolType);
			PackLogger packLogger = this.config.getPackLogger();
			packLogger.logPack(pack, packLen, buff);
		}
		
		return out;
	}

	

}
