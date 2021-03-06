package com.ggx.core.common.handler.codec.impl;

import java.util.Arrays;

import org.apache.commons.compress.utils.ByteUtils;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.encryption.aes.AESCipher;
import com.ggx.core.common.handler.codec.DecodeHandler;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.utils.ByteArrayTransferUtil;
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
 *             | _________________ AES CONTENT ________________|
 * @author zai
 *
 */
public class AESSupportDecodeHandler implements DecodeHandler {

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
	

	public AESSupportDecodeHandler() {

	}

	public AESSupportDecodeHandler(GGConfig config) {
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
		
		AESCipher aesCipher = this.config.getAesCipher();
		
		byte[] buff = new byte[packLen];
		in.readBytes(buff);
		
		//AES解码
		buff = aesCipher.decrypt(buff);
		packLen = buff.length;
		
		int buffReadIndex = 0;
		
		//读取预留内容 2 字节
		ByteArrayTransferUtil.bytesToUnsignedShort(buff, buffReadIndex);
		
		buffReadIndex += 2;
		
		//读取指令长度标识1字节
		int actionLen = buff[buffReadIndex];
		buffReadIndex += 1;
		
		byte[] action = ByteArrayTransferUtil.readBytes(buff, buffReadIndex, buffReadIndex + actionLen);
		buffReadIndex += actionLen;

		// 读取数据体 = 总包长 - 标识长度占用字节 - 标识体占用字节数
		int bodyLen = packLen - ALL_TAG_LEN - actionLen;
		byte[] message = null;
		if (bodyLen != 0) {
			// 读取数据体部分byte数组
			message = ByteArrayTransferUtil.readBytes(buff, buffReadIndex, buffReadIndex + bodyLen);
			//packBytesReadIndex += bodyLen;
		}

		Pack pack = new Pack(action, message);
		pack.setProtocolType(protocolType);
		
		Channel channel = ctx.channel();
		pack.setChannel(channel);
		
		// 获取session
		pack.setSession(config.getSessionFactory().getSession(channel));

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
