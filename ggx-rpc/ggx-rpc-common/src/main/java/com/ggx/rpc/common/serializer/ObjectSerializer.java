package com.ggx.rpc.common.serializer;

import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.handler.serializer.impl.ProtoStuffSerializer;

/**
 * Double序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class ObjectSerializer implements ParameterSerializer<Object>{
	
	private Serializer serializer = new ProtoStuffSerializer();
	

	@Override
	public byte[] serialize(Object param) throws Exception {
		if (param == null) {
			return null;
		}
		return serializer.serialize(param);
	}

	@Override
	public Object deserialize(byte[] bytes, Class<Object> t) throws Exception {
		return serializer.deserialize(bytes, t);
	}


}
