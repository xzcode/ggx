package com.ggx.monitor.common.data.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.monitor.common.data.model.service.ServiceData;
import com.ggx.registry.common.service.ServiceInfo;

public class ServerDataManager {
	
	/**
	 * 服务数据集合
	 */
	private Map<String, ServiceData> serviceDatas = new ConcurrentHashMap<>();
	
}
