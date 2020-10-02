package com.ggx.rpc.common.serializer;

import com.ggx.core.common.utils.ByteArrayTransferUtil;

/**
 * Float序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class FloatSerializer implements ParameterSerializer<Float>{
	

	@Override
	public byte[] serialize(Float param) throws Exception {
		if (param == null) {
			return null;
		}
		return ByteArrayTransferUtil.floatToBytes(param);
	}

	@Override
	public Float deserialize(byte[] bytes, Class<Float> t) throws Exception {
		return ByteArrayTransferUtil.bytesToFloat(bytes);
	}


}
