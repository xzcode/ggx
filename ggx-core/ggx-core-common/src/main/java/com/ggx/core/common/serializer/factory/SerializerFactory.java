package com.ggx.core.common.serializer.factory;

import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.serializer.impl.JsonSerializer;
import com.ggx.core.common.serializer.impl.KryoSerializer;
import com.ggx.core.common.serializer.impl.ProtoStuffSerializer;

/**
 * 序列化器工厂类
 *
 * @author zai
 * 2017-08-12 13:49:53
 */
public class SerializerFactory {
	
	public static final Serializer JSON_SERIALIZER = new JsonSerializer();
	
	public static final Serializer PROTO_STUFF_SERIALIZER = new ProtoStuffSerializer();
	
	public static final Serializer KRYO_SERIALIZER = new KryoSerializer();
	
	/**
	 * 序列化器类型定义
	 *
	 * @author zai
	 * 2017-08-12 13:47:25
	 */
	public static interface SerializerType{
		
		String JSON = "json";
		
		String PROTO_STUFF = "protostuff";
		
		String KRYO = "kryo";
		
	}
	
	/**
	 * 获取序列化器
	 * @return 返回指定的序列化器实现，默认为json序列化器
	 * @author zai
	 * 2017-08-12 13:50:07
	 */
	public static Serializer getSerializer(String serializerType) {
		
		switch (serializerType) {
		
		case SerializerType.JSON:
			return JSON_SERIALIZER;
			
		case SerializerType.PROTO_STUFF:
			return PROTO_STUFF_SERIALIZER;
			
		case SerializerType.KRYO:
			return KRYO_SERIALIZER;
			
		default:
			return null;
		}
		
	}
	
	/**
	 * 通过序列化器获取序列化方式
	 *
	 * @param serializer
	 * @return
	 * @author zai
	 * 2020-05-14 17:18:36
	 */
	public static String getSerializerType(Serializer serializer) {
		
		if (serializer == PROTO_STUFF_SERIALIZER || serializer.getClass() == PROTO_STUFF_SERIALIZER.getClass()) {
			return SerializerType.PROTO_STUFF;
		}
		
		if (serializer == JSON_SERIALIZER || serializer.getClass() == JSON_SERIALIZER.getClass()) {
			return SerializerType.JSON;
		}
		
		if (serializer == KRYO_SERIALIZER || serializer.getClass() == KRYO_SERIALIZER.getClass()) {
			return SerializerType.KRYO;
		}
		return null;
		
	}

}
