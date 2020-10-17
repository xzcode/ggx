package com.ggx.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GGX反射工具
 *
 * @author zai
 * 2020-10-04 00:26:59
 */
public class GGXReflectUtil {
	
	/**
	 * 获取所有父类class
	 *
	 * @param clazz
	 * @return
	 * @author zai
	 * 2020-10-04 00:27:09
	 */
	public static List<Class<?>> getAllSuperClasses(Class<?> clazz, boolean includeSelf) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		if (includeSelf) {
			list.add(clazz);
		}
		Class<?> superclass = clazz;
		do {
			superclass = superclass.getSuperclass();
			if (superclass != null && superclass != Object.class) {
				list.add(superclass);
			}
		} while (superclass != null && superclass != Object.class);
		Collections.reverse(list);
		return list;
	}
	
	/**
	 * 获取所有声明的方法
	 *
	 * @param clazz
	 * @return
	 * @author zai
	 * 2020-10-04 00:39:08
	 */
	public static List<Method> getAllDeclaredMethods(Class<?> clazz) {
		List<Class<?>> classes = getAllSuperClasses(clazz, true);
		List<Method> list = new ArrayList<>();
		for (Class<?> cla : classes) {
			Method[] methods = cla.getDeclaredMethods();
			for (Method method : methods) {
				list.add(method);
			}
		}
		return list;
	}
	
	/**
	 * 获取所有声明的属性
	 *
	 * @param clazz
	 * @return
	 * @author zai
	 * 2020-10-04 00:39:08
	 */
	public static List<Field> getAllDeclaredFields(Class<?> clazz) {
		List<Class<?>> classes = getAllSuperClasses(clazz, true);
		List<Field> list = new ArrayList<>();
		for (Class<?> cla : classes) {
			Field[] fields = cla.getDeclaredFields();
			for (Field field : fields) {
				list.add(field);
			}
		}
		return list;
	}
	

}
