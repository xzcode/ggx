package com.ggx.rpc.common.serializer.factory.impl;

import com.ggx.rpc.common.serializer.ParameterSerializer;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.rpc.common.serializer.impl.ObjectSerializer;

public class DefaultParameterSerializerFactory implements ParameterSerializerFactory {
	/*
	 * private static final StringSerializer STRING_SERIALIZER = new
	 * StringSerializer(); private static final IntegerSerializer INTEGER_SERIALIZER
	 * = new IntegerSerializer(); private static final LongSerializer
	 * LONG_SERIALIZER = new LongSerializer(); private static final ShortSerializer
	 * SHORT_SERIALIZER = new ShortSerializer(); private static final
	 * DoubleSerializer DOUBLE_SERIALIZER = new DoubleSerializer(); private static
	 * final FloatSerializer FLOAT_SERIALIZER = new FloatSerializer();
	 */
	private static final ObjectSerializer OBJECT_SERIALIZER = new  ObjectSerializer();

	@Override
	public ParameterSerializer<?> getSerializer(Class<?> paramType) {
		return OBJECT_SERIALIZER;
	}

	@Override
	public ParameterSerializer<?> getDefaultSerializer() {
		return OBJECT_SERIALIZER;
	}

}
