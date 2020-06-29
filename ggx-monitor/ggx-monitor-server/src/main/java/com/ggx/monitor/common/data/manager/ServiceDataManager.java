package com.ggx.monitor.common.data.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.monitor.common.data.model.service.ServiceData;
import com.ggx.util.common.SimpleMapManager;

public class ServiceDataManager implements SimpleMapManager<String, ServiceData>{
	
	/**
	 * 服务数据集合
	 */
	private Map<String, ServiceData> datas = new ConcurrentHashMap<>();

	@Override
	public Map<String, ServiceData> getMap() {
		return datas;
	}
	
}
