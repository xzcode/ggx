package com.ggx.rpc.common.serializer.factory.impl;

import com.ggx.rpc.common.serializer.ParameterSerializer;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.rpc.common.serializer.impl.DoubleSerializer;
import com.ggx.rpc.common.serializer.impl.FloatSerializer;
import com.ggx.rpc.common.serializer.impl.IntegerSerializer;
import com.ggx.rpc.common.serializer.impl.LongSerializer;
import com.ggx.rpc.common.serializer.impl.ObjectSerializer;
import com.ggx.rpc.common.serializer.impl.ShortSerializer;
import com.ggx.rpc.common.serializer.impl.StringSerializer;

public class DefaultParameterSerializerFactory implements ParameterSerializerFactory {
	
	private static final StringSerializer STRING_SERIALIZER = new  StringSerializer();
	private static final IntegerSerializer INTEGER_SERIALIZER = new  IntegerSerializer();
	private static final LongSerializer LONG_SERIALIZER = new  LongSerializer();
	private static final ShortSerializer SHORT_SERIALIZER = new  ShortSerializer();
	private static final DoubleSerializer DOUBLE_SERIALIZER = new  DoubleSerializer();
	private static final FloatSerializer FLOAT_SERIALIZER = new  FloatSerializer();
	private static final ObjectSerializer OBJECT_SERIALIZER = new  ObjectSerializer();

	@Override
	public ParameterSerializer<?> getSerializer(Class<?> paramType) {
		
		if (paramType == String.class) {
			return STRING_SERIALIZER;
		}
		if (paramType == Integer.class || paramType == int.class) {
			return INTEGER_SERIALIZER;
		}
		if (paramType == Long.class || paramType == long.class) {
			return LONG_SERIALIZER;
		}
		if (paramType == Double.class || paramType == double.class) {
			return DOUBLE_SERIALIZER;
		}
		if (paramType == Short.class || paramType == short.class) {
			return SHORT_SERIALIZER;
		}
		if (paramType == Float.class || paramType == float.class) {
			return FLOAT_SERIALIZER;
		}
		
		return OBJECT_SERIALIZER;
	}

}
