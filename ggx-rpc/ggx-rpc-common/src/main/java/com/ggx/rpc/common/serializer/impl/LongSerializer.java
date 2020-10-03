package com.ggx.rpc.common.serializer.impl;

import com.ggx.core.common.utils.ByteArrayTransferUtil;
import com.ggx.rpc.common.serializer.ParameterSerializer;

/**
 * Long序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class LongSerializer implements ParameterSerializer<Long>{
	

	@Override
	public byte[] serialize(Object param) throws Exception {
		if (param == null) {
			return null;
		}
		return ByteArrayTransferUtil.longToBytes((long) param);
	}

	@Override
	public Long deserialize(byte[] bytes, Class<?> t) throws Exception {
		return ByteArrayTransferUtil.bytesToLong(bytes);
	}


}
