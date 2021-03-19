package com.ggx.util.manager.list.listener.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.util.manager.list.listener.Listener;
import com.ggx.util.manager.list.listener.ListenerManager;

/**
 * 默认监听器管理器
 *
 * @param <K, V>
 * @author zai
 * 2020-09-15 17:40:26
 */
public class DefaultListenerManager<T> implements ListenerManager<T>{
	
	protected List<Listener<T>> mapDataListeners = new CopyOnWriteArrayList<>();

	@Override
	public List<Listener<T>> getListeners() {
		return mapDataListeners;
	}

}
