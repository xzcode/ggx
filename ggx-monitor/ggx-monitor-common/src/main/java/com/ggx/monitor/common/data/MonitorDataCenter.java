package com.ggx.monitor.common.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MonitorDataCenter {
	
	private Map<Class<?>, Object> dataManagers = new ConcurrentHashMap<>();
	
	
	/**
	 * 获取指定的数据管理器
	 *
	 * @param <T>
	 * @param clazz
	 * @return
	 * @author zai
	 * 2020-06-24 18:05:52
	 */
	@SuppressWarnings("unchecked")
	public <T> T getDataManager(Class<T> clazz) {
		return (T) dataManagers.get(clazz);
	}
	
	/**
	 * 添加数据管理器
	 *
	 * @param dataManager
	 * @author zai
	 * 2020-06-24 18:06:53
	 */
	public void addDataManager(Object dataManager) {
		this.dataManagers.put(dataManager.getClass(), dataManager);
		
	}
	

}
