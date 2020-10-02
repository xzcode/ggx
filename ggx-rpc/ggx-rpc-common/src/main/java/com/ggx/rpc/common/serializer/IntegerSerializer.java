package com.ggx.rpc.common.serializer;

import com.ggx.core.common.utils.ByteArrayTransferUtil;

/**
 * 字符串序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class IntegerSerializer implements ParameterSerializer<Integer>{
	
	@Override
	public byte[] serialize(Integer param) throws Exception {
		if (param == null) {
			return null;
		}
		return ByteArrayTransferUtil.intToBytes(param);
	}

	@Override
	public Integer deserialize(byte[] bytes, Class<Integer> t) throws Exception {
		return ByteArrayTransferUtil.bytesToInt(bytes);
	}


}
