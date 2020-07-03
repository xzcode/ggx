package com.ggx.admin.collector.server.service.basic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.ggx.admin.collector.server.constant.GGXAdminCollectorServerSessionKeys;
import com.ggx.admin.common.collector.data.model.service.ServiceData;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.spring.support.annotation.GGXEventHandler;

@GGXEventHandler(GGEvents.Connection.CLOSED)
public abstract class BasicDataService<T> implements EventListener<Void>{
	
	private Map<String, T> dataCache = new ConcurrentHashMap<String, T>();
	
	
	public void addData(String serviceId, T data) {
		this.dataCache.put(serviceId, data);
	}
	
	public List<T> getDataList() {
		return this.dataCache.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
	}
	
	@Override
	public void onEvent(EventData<Void> eventData) {
		GGSession session = eventData.getSession();
		String serviceId = session.getAttribute(GGXAdminCollectorServerSessionKeys.SERVICE_ID, String.class);
		this.dataCache.remove(serviceId);
	}
	
	

}
