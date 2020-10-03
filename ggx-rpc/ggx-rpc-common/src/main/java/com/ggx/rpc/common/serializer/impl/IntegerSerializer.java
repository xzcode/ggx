package com.ggx.rpc.common.serializer.impl;

import com.ggx.core.common.utils.ByteArrayTransferUtil;
import com.ggx.rpc.common.serializer.ParameterSerializer;

/**
 * 字符串序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class IntegerSerializer implements ParameterSerializer<Integer>{
	
	@Override
	public byte[] serialize(Object param) throws Exception {
		if (param == null) {
			return null;
		}
		return ByteArrayTransferUtil.intToBytes((int) param);
	}

	@Override
	public Integer deserialize(byte[] bytes, Class<?> t) throws Exception {
		return ByteArrayTransferUtil.bytesToInt(bytes);
	}


}
