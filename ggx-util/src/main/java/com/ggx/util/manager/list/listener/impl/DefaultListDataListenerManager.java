package com.ggx.util.manager.list.listener.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.util.manager.list.listener.ListDataListener;
import com.ggx.util.manager.list.listener.ListDataListenerManager;

/**
 * 默认监听器管理器
 *
 * @param <K, V>
 * @author zai
 * 2020-09-15 17:40:26
 */
public class DefaultListDataListenerManager<T> implements ListDataListenerManager<T>{
	
	protected List<ListDataListener<T>> mapDataListeners = new CopyOnWriteArrayList<>();

	@Override
	public List<ListDataListener<T>> getListeners() {
		return mapDataListeners;
	}

}
