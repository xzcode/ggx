package com.ggx.util.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.beans.BeanCopier;

/**
 * 
 * GGXBeanUtils
 * @author zai
 * 2020-10-7 11:39:00
 */
public class GGXBeanUtils {
	
	private static final Map<String, BeanCopier> cache = new ConcurrentHashMap<String, BeanCopier>(128);
	
	protected static final BeanCopier getCache(Class<?> src, Class<?> target) {
		BeanCopier beanCopier = cache.get(generateKey(src, target));
		if (beanCopier == null) {
			beanCopier = BeanCopier.create(src.getClass(), target.getClass(), false);
			cache.put(generateKey(src, target), beanCopier);
		}
		return beanCopier;
	}
	
	/**
	 * 生成缓存key
	 * @param src
	 * @param target
	 * @return
	 * @author zai
	 * 2020-10-7 11:38:50
	 */
	protected static String generateKey(Class<?> src, Class<?> target) {
		return src.getCanonicalName() + "-" + target.getCanonicalName();
	}

	/**
	 * 属性复制
	 * @param src
	 * @param target
	 * @author zai
	 * 2020-10-7 11:38:00
	 */
	public static void copyProperties(Object src, Object target) {
		getCache(src.getClass(), target.getClass()).copy(src, target, null);
	}

}
