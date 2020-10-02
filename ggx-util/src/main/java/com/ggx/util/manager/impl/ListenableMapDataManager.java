package com.ggx.util.manager.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.util.manager.MapDataManager;
import com.ggx.util.manager.listener.Listener;
import com.ggx.util.manager.listener.ListenerManager;

public abstract class ListenableMapDataManager<K, V> implements MapDataManager<K, V>{
	
	protected Map<K, V> map = new ConcurrentHashMap<>();
	
	private ListenerManager<V> onPutListenerManager = new DefaultListenerManager<>();
	
	private ListenerManager<V> onRemoveListenerManager = new DefaultListenerManager<>();
	
	@Override
	public Map<K, V> getMap() {
		return map;
	}
	
	public void onPut(Listener<V> listener) {
		this.onPutListenerManager.addListener(listener);
	}
	
	public void onRemove(Listener<V> listener) {
		this.onRemoveListenerManager.addListener(listener);
	}

	@Override
	public V put(K key, V value) {
		V oldV = MapDataManager.super.put(key, value);
		this.onPutListenerManager.triggerListeners(value);
		if (oldV != null) {
			this.onRemoveListenerManager.triggerListeners(oldV);
		}
		return oldV;
	}

	@Override
	public V remove(K key) {
		V removedValue = MapDataManager.super.remove(key);
		this.onRemoveListenerManager.triggerListeners(removedValue);
		return removedValue;
	}
	
	

}
