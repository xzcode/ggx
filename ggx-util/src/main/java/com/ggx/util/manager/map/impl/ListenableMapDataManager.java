package com.ggx.util.manager.map.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.util.manager.map.MapDataManager;
import com.ggx.util.manager.map.listener.MapDataListener;
import com.ggx.util.manager.map.listener.MapDataListenerManager;
import com.ggx.util.manager.map.listener.impl.DefaultMapDataListenerManager;

public abstract class ListenableMapDataManager<K, V> implements MapDataManager<K, V>{
	
	protected transient Map<K, V> map = new ConcurrentHashMap<>();
	
	private transient MapDataListenerManager<K, V> onPutListenerManager = new DefaultMapDataListenerManager<>();
	
	private transient MapDataListenerManager<K, V> onRemoveListenerManager = new DefaultMapDataListenerManager<>();
	
	@Override
	public Map<K, V> getMap() {
		return map;
	}
	
	public void onPut(MapDataListener<K, V> listener) {
		this.onPutListenerManager.addListener(listener);
	}
	
	public void onRemove(MapDataListener<K, V> listener) {
		this.onRemoveListenerManager.addListener(listener);
	}

	@Override
	public V put(K key, V value) {
		V oldV = MapDataManager.super.put(key, value);
		this.onPutListenerManager.triggerListeners(key, value);
		if (oldV != null) {
			this.onRemoveListenerManager.triggerListeners(key, oldV);
		}
		return oldV;
	}
	
	@Override
	public V putIfAbsent(K key, V value) {
		V returnObj = MapDataManager.super.putIfAbsent(key, value);
		if (returnObj == null) {
			this.onPutListenerManager.triggerListeners(key, value);
		}
		return returnObj;
	}
	
	
	@Override
	public V get(K key) {
		return this.getMap().get(key);
	}
	

	@Override
	public V remove(K key) {
		V removedValue = MapDataManager.super.remove(key);
		this.onRemoveListenerManager.triggerListeners(key, removedValue);
		return removedValue;
	}
	
	

}
