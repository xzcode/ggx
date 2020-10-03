package com.ggx.rpc.common.serializer.impl;

import java.nio.charset.Charset;

import com.ggx.rpc.common.serializer.ParameterSerializer;

/**
 * 字符串序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class StringSerializer implements ParameterSerializer<String>{
	
	private Charset charset = Charset.forName("utf-8");

	@Override
	public byte[] serialize(Object param) throws Exception {
		if (param == null) {
			return null;
		}
		return ((String) param).getBytes(charset);
	}

	@Override
	public String deserialize(byte[] bytes, Class<?> t) throws Exception {
		return new String(bytes, charset);
	}


}
