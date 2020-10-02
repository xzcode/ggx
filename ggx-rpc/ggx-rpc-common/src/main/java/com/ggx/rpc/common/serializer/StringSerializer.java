package com.ggx.rpc.common.serializer;

import java.nio.charset.Charset;

/**
 * 字符串序列化器
 * 
 * @author zai
 * 2020-10-2 21:09:35
 */
public class StringSerializer implements ParameterSerializer<String>{
	
	private Charset charset = Charset.forName("utf-8");

	@Override
	public byte[] serialize(String param) throws Exception {
		if (param == null) {
			return null;
		}
		return param.getBytes(charset);
	}

	@Override
	public String deserialize(byte[] bytes, Class<String> t) throws Exception {
		return new String(bytes, charset);
	}


}
