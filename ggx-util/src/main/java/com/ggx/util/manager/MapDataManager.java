package com.ggx.util.manager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Map管理器接口
 *
 * @author zai
 * 2020-09-15 14:28:15
 */
public interface MapDataManager<K, V> {
	
	
	
	Map<K, V> getMap();
	
	/**
	 * 添加
	 *
	 * @param key 键
	 * @param value 值
	 * @author zai
	 * 2020-09-15 18:07:43
	 */
	default V put(K key, V value) {
		return getMap().put(key, value);
	}
	
	/**
	 * 获取
	 * @param key
	 * @return
	 * @author zai
	 * 2020-10-3 11:47:22
	 */
	default V get(K key) {
		return getMap().get(key);
	}
	
	/**
	 * 移除
	 *
	 * @param key
	 * @author zai
	 * 2020-09-15 16:49:08
	 */
	default V remove(K key) {
		return getMap().remove(key);
	}
	
	/**
	 * 随机获取一个
	 * @return
	 * @author zai
	 * 2020-10-2 14:35:04
	 */
	@SuppressWarnings("unchecked")
	default V getRandomOne() {
		Set<Entry<K, V>> entrySet = getMap().entrySet();
		Object[] array = entrySet.toArray();
		if (array.length == 1) {
			return ((Entry<K, V>) array[0]).getValue();
		}
		if (array.length > 1) {
			return ((Entry<K, V>) array[ThreadLocalRandom.current().nextInt(array.length)]).getValue();
		}
		return null;
	}
	
	/**
	 * 获取map中元素个数
	 * @return
	 * @author zai
	 * 2020-10-3 15:19:43
	 */
	default int size() {
		return this.getMap().size();
	}

}
