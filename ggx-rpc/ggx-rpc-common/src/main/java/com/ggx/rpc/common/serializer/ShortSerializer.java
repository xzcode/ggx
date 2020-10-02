package com.ggx.rpc.common.serializer;

import com.ggx.core.common.utils.ByteArrayTransferUtil;

/**
 * Short序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class ShortSerializer implements ParameterSerializer<Short>{
	

	@Override
	public byte[] serialize(Short param) throws Exception {
		if (param == null) {
			return null;
		}
		return ByteArrayTransferUtil.shortToBytes(param);
	}

	@Override
	public Short deserialize(byte[] bytes, Class<Short> t) throws Exception {
		return ByteArrayTransferUtil.bytesToShort(bytes);
	}


}
