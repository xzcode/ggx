package com.ggx.rpc.client.service.fallback;

/**
 * 备用实例工厂接口
 * 
 * @author zai
 * 2020-10-4 11:31:55
 */
public interface FallbackInstanceFactory {
	
	Object instant(Class<?> clazz);

}
