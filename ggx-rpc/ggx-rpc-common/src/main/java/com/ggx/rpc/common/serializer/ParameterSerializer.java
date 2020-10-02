package com.ggx.rpc.common.serializer;

public interface ParameterSerializer<T> {
	
	byte[] serialize(T param) throws Exception;

	T deserialize(byte[] bytes, Class<T> t) throws Exception;


}
