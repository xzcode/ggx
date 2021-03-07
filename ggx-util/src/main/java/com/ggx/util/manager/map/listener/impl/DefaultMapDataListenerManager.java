package com.ggx.util.manager.map.listener.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.util.manager.map.listener.MapDataListener;
import com.ggx.util.manager.map.listener.MapDataListenerManager;

/**
 * 默认监听器管理器
 *
 * @param <K, V>
 * @author zai
 * 2020-09-15 17:40:26
 */
public class DefaultMapDataListenerManager<K, V> implements MapDataListenerManager<K, V>{
	
	protected List<MapDataListener<K, V>> mapDataListeners = new CopyOnWriteArrayList<>();

	@Override
	public List<MapDataListener<K, V>> getListeners() {
		return mapDataListeners;
	}

}
