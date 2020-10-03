package com.ggx.rpc.common.serializer;

public interface ParameterSerializer<T> {
	
	byte[] serialize(Object param) throws Exception;

	T deserialize(byte[] bytes, Class<?> t) throws Exception;


}
