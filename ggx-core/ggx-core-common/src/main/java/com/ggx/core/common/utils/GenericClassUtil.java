package com.ggx.core.common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型类工具
 *
 * @author zai
 * 2020-04-11 23:17:18
 */
public class GenericClassUtil {
	
	/**
	 * 获取泛型的class
	 *
	 * @param targetClass
	 * @return
	 * @author zai
	 * 2020-04-11 23:16:33
	 */
	public static Class<?> getInterfaceGenericClass(Class<?> targetClass) {
		Class<?> msgClass = null;
		
		Type[] genericInterfaces = targetClass.getGenericInterfaces();
		if (genericInterfaces == null || genericInterfaces.length == 0) {
			ParameterizedType superParameterizedType = (ParameterizedType)targetClass.getGenericSuperclass();
			msgClass = (Class<?>) superParameterizedType.getActualTypeArguments()[0];
		}else {
			msgClass = (Class<?>) ((ParameterizedType)genericInterfaces[0]).getActualTypeArguments()[0];
		}
		return msgClass;
	}

}
