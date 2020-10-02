package com.ggx.rpc.common.serializer;

import com.ggx.core.common.utils.ByteArrayTransferUtil;

/**
 * Long序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class LongSerializer implements ParameterSerializer<Long>{
	

	@Override
	public byte[] serialize(Long param) throws Exception {
		if (param == null) {
			return null;
		}
		return ByteArrayTransferUtil.longToBytes(param);
	}

	@Override
	public Long deserialize(byte[] bytes, Class<Long> t) throws Exception {
		return ByteArrayTransferUtil.bytesToLong(bytes);
	}


}
