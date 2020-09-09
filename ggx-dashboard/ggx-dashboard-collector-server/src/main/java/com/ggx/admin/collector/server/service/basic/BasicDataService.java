package com.ggx.admin.collector.server.service.basic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class BasicDataService<T>{
	
	private Map<String, T> dataCache = new ConcurrentHashMap<String, T>();
	
	
	public void addData(String serviceId, T data) {
		this.dataCache.put(serviceId, data);
	}
	
	public List<T> getDataList() {
		return this.dataCache.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
	}
	
	public T getData(String serviceId) {
		return this.dataCache.get(serviceId);
	}

}
