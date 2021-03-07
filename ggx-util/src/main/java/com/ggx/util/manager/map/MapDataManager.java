package com.ggx.util.manager.map;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.ggx.util.interfaces.ForEach;

/**
 * Map管理器接口
 *
 * @author zai
 * 2020-09-15 14:28:15
 */
public interface MapDataManager <K, V> {

	Map<K, V> getMap();

	/**
	 * 添加
	 *
	 * @param key   键
	 * @param value 值
	 * @author zai 2020-09-15 18:07:43
	 */
	default V put(K key, V value) {
		return getMap().put(key, value);
	}
	
	/**
	 * 当不存在的时候进行添加
	 *
	 * @param key
	 * @param value
	 * @return
	 * 2021-01-13 11:25:51
	 */
	default V putIfAbsent(K key, V value) {
		return getMap().putIfAbsent(key, value);
	}

	/**
	 * 获取
	 * 
	 * @param key
	 * @return
	 * @author zai 2020-10-3 11:47:22
	 */
	default V get(K key) {
		return getMap().get(key);
	}

	/**
	 * 移除
	 *
	 * @param key
	 * @author zai 2020-09-15 16:49:08
	 */
	default V remove(K key) {
		return getMap().remove(key);
	}

	/**
	 * 随机获取一个
	 * 
	 * @return
	 * @author zai 2020-10-2 14:35:04
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
	 * 
	 * @return
	 * @author zai 2020-10-3 15:19:43
	 */
	default int size() {
		return this.getMap().size();
	}

	/**
	 * foreach遍历
	 *
	 * @param one
	 * 2020-11-30 18:08:45
	 */
	default void ForEach(ForEach<V> one) {
		this.getMap().entrySet().forEach(e -> one.each(e.getValue()));
	}
	
	/**
	 * 获取list
	 *
	 * @return
	 * 2021-01-18 11:06:44
	 */
	default List<V> getList() {
		return this.getMap().entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
	}

}