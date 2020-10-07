package com.ggx.util.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.util.logger.GGXLogUtil;

import net.sf.cglib.beans.BeanCopier;

/**
 * 
 * GGXBeanUtils
 * @author zai
 * 2020-10-7 11:39:00
 */
public class GGXBeanUtils {
	
	private static final Map<String, BeanCopier> CACHE = new ConcurrentHashMap<>(128);
	
	protected static final BeanCopier getCache(Class<?> srcClass, Class<?> targetClass) {
		String key = generateKey(srcClass, targetClass);
		BeanCopier beanCopier = CACHE.get(key);
		if (beanCopier == null) {
			beanCopier = BeanCopier.create(srcClass, targetClass, false);
			CACHE.put(key, beanCopier);
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
	
	/**
	 * 属性复制
	 * @param <T>
	 * @param src
	 * @param targetClass
	 * @return
	 * @author zai
	 * 2020-10-7 12:01:08
	 */
	public static <T> T copyProperties(Object src, Class<T> targetClass) {
		T target = null;
		try {
			target = targetClass.newInstance();
			getCache(src.getClass(), targetClass).copy(src, target, null);
		} catch (Exception e) {
			GGXLogUtil.getLogger(GGXBeanUtils.class).error("GGXBeanUtils copy properties Error!", e);
		}
		return target;
	}

}
