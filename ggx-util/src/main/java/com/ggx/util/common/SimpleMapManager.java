package com.ggx.util.common;

import java.util.Map;

/**
 * 普通map数据管理器
 *
 * @param <K>
 * @param <V>
 * @author zai
 * 2020-06-24 16:09:07
 */
public interface SimpleMapManager<K, V> {
	
	
	
	
	/**
	 * 获取map集合
	 *
	 * @return
	 * @author zai
	 * 2020-06-24 16:10:04
	 */
	Map<K, V> getMap();
	
	
	/**
	 * 获取对应key 的值
	 *
	 * @param key
	 * @return
	 * @author zai
	 * 2020-06-24 16:14:53
	 */
	default V get(K key) {
		return getMap().get(key);
	}
	
	
	default V put(K key, V value) {
		return getMap().put(key, value);
	}
	
	/**
	 * 如果不key则存入
	 *
	 * @param key
	 * @param value
	 * @return 返回已存在集合中的值，如果是第一次存入，返回null
	 * @author zai
	 * 2020-06-24 16:19:58
	 */
	default V putIfAbsent(K key, V value) {
		return getMap().putIfAbsent(key, value);
	}
	
	/**
	 * 替换指定的key对应的value
	 *
	 * @param key
	 * @param value
	 * @return 返回被替换走的key对应的value，（如果返回null，可能不存在该key，或者存在key但value值为null）
	 * @author zai
	 * 2020-06-24 16:19:58
	 */
	default V replace(K key, V value) {
		return getMap().replace(key, value);
	}
	
	

}
