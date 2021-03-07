package com.ggx.util.manager.map.listener;

/**
 * 取消监听器
 *
 * @param <T>
 * @author zai
 * 2020-09-15 16:53:56
 */
public interface MapDataListener<K, V> {
	
	/**
	 * 取消动作
	 *
	 * @param data
	 * @author zai
	 * 2020-09-15 16:54:12
	 */
	void onTrigger(K key, V data);

}
