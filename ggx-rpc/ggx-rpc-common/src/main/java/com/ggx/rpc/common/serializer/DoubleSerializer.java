package com.ggx.rpc.common.serializer;

import com.ggx.core.common.utils.ByteArrayTransferUtil;

/**
 * Double序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class DoubleSerializer implements ParameterSerializer<Double>{
	

	@Override
	public byte[] serialize(Double param) throws Exception {
		if (param == null) {
			return null;
		}
		return ByteArrayTransferUtil.doubleToBytes(param);
	}

	@Override
	public Double deserialize(byte[] bytes, Class<Double> t) throws Exception {
		return ByteArrayTransferUtil.bytesToDouble(bytes);
	}


}
