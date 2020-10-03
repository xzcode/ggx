package com.ggx.rpc.common.serializer.impl;

import com.ggx.core.common.utils.ByteArrayTransferUtil;
import com.ggx.rpc.common.serializer.ParameterSerializer;

/**
 * Short序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class ShortSerializer implements ParameterSerializer<Short>{
	

	@Override
	public byte[] serialize(Object param) throws Exception {
		if (param == null) {
			return null;
		}
		return ByteArrayTransferUtil.shortToBytes((short) param);
	}

	@Override
	public Short deserialize(byte[] bytes, Class<?> t) throws Exception {
		return ByteArrayTransferUtil.bytesToShort(bytes);
	}


}
