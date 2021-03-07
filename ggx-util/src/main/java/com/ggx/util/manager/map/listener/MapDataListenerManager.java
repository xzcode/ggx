package com.ggx.util.manager.map.listener;

import java.util.List;

import com.ggx.util.logger.GGXLogUtil;

/**
 * 取消行为支持接口
 *
 * @param <T>
 * @author zai
 * 2020-09-15 16:55:47
 */
public interface MapDataListenerManager<K, V> {
	
	/**
	 * 获取所有监听器
	 *
	 * @return
	 * @author zai
	 * 2020-09-15 16:58:42
	 */
	List<MapDataListener<K, V>> getListeners();
	
	
	/**
	 * 添加取消监听器
	 *
	 * @param listener
	 * @author zai
	 * 2020-09-15 16:55:56
	 */
	default void addListener(MapDataListener<K, V>  listener) {
		getListeners().add(listener);
	}
	
	/**
	 * 移除监听器
	 *
	 * @param listener
	 * @author zai
	 * 2020-09-15 16:56:42
	 */
	default void removeListener(MapDataListener<K, V>  listener) {
		getListeners().remove(listener);
	}
	
	/**
	 * 触发所有监听器
	 *
	 * @param listener
	 * @author zai
	 * 2020-09-15 17:15:16
	 */
	default void triggerListeners(K key, V data) {
		this.triggerListeners(key, data, true);
	}
	
	/**
	 * 触发所有监听器
	 *
	 * @param data
	 * @param continueOnError 遇到错误是否继续触发后续监听器
	 * @author zai
	 * 2020-09-15 17:38:15
	 */
	default void triggerListeners(K key, V data, boolean continueOnError) {
		List<MapDataListener<K, V> > listeners = getListeners();
		for (MapDataListener<K, V>  listener : listeners) {
			if (continueOnError) {
				try {
					listener.onTrigger(key, data);
				} catch (Exception e) {
					GGXLogUtil.getLogger(this).error("Trigger Listener Error!", e);
				}
			}else {
				triggerListeners(key, data);
			}
		}
	}
	
	
	
}
