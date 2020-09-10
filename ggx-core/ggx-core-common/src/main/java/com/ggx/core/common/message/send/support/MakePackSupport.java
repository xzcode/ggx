package com.ggx.core.common.message.send.support;

import java.nio.charset.Charset;

import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.handler.serializer.factory.SerializerFactory;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.constant.GGDefaultSessionKeys;
import com.ggx.core.common.utils.logger.GGLoggerUtil;

/**
 * 生成字节码数据包支持
 * 
 * 
 * @author zai
 * 2019-11-27 21:59:29
 */
public interface MakePackSupport{
	
	/**
	 * 获取字符串编码对象
	 * 
	 * @return
	 * @author zai
	 * 2019-12-11 14:24:30
	 */
	Charset getCharset();
	
	/**
	 * 获取序列化器
	 * 
	 * @return
	 * @author zai
	 * 2019-12-11 14:24:23
	 */
	Serializer getSerializer();
	
	/**
	 * 生成字节码数据包
	 * 
	 * @param messageData
	 * @return
	 * 
	 * @author zai 2019-11-24 17:28:57
	 */
	default Pack makePack(MessageData<?> messageData) {
		try {
			GGXSession session = messageData.getSession();
			String serType = null;
			if (session != null) {
				serType = session.getAttribute(GGDefaultSessionKeys.SERIALIZE_TYPE, String.class);
			}
			Serializer serializer;
			Pack pack = new Pack();
			if (serType != null) {
				serializer = SerializerFactory.getSerializer(serType);
				pack.setSerializeType(serType);
			}else {
				serializer = getSerializer();
			}
			byte[] actionIdBytes = messageData.getAction().getBytes(getCharset());
			byte[] messageBytes = messageData.getMessage() == null ? null : serializer.serialize(messageData.getMessage());
			pack.setSession(session);
			pack.setAction(actionIdBytes);
			pack.setMessage(messageBytes);
			
			return pack;
		} catch (Exception e) {
			GGLoggerUtil.getLogger().error("Make pack Error!", e);
		}
		return null;
	}
}
