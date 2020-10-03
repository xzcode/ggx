package com.ggx.rpc.common.serializer.factory;

import com.ggx.rpc.common.serializer.ParameterSerializer;

public interface ParameterSerializerFactory {
	
	
	
	 ParameterSerializer<?> getSerializer(Class<?> paramType);

}
