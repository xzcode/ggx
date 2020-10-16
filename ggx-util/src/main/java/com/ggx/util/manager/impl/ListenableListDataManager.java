package com.ggx.util.manager.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.util.manager.ListDataManager;
import com.ggx.util.manager.listener.Listener;
import com.ggx.util.manager.listener.ListenerManager;

public abstract class ListenableListDataManager<T> implements ListDataManager<T>{
	
	protected List<T> list = new CopyOnWriteArrayList<>();
	
	private ListenerManager<T> onPutListenerManager = new DefaultListenerManager<>();
	
	private ListenerManager<T> onRemoveListenerManager = new DefaultListenerManager<>();
	
	@Override
	public List<T> getList() {
		return list;
	}
	
	public void onAdd(Listener<T> listener) {
		this.onPutListenerManager.addListener(listener);
	}
	
	public void onRemove(Listener<T> listener) {
		this.onRemoveListenerManager.addListener(listener);
	}

	@Override
	public void add(T obj) {
		if (obj == null) {
			return;
		}
		boolean add = this.list.add(obj);
		this.onPutListenerManager.triggerListeners(obj);
		if (add) {
			this.onRemoveListenerManager.triggerListeners(obj);
		}
	}
	

	@Override
	public T remove(int index) {
		T removedValue = this.list.remove(index);
		this.onRemoveListenerManager.triggerListeners(removedValue);
		return removedValue;
	}
	
	@Override
	public boolean remove(T obj) {
		boolean removed = this.list.remove(obj);
		this.onRemoveListenerManager.triggerListeners(obj);
		return removed;
	}
	
	

}
