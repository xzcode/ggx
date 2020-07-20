package com.ggx.admin.server.listener.service.counter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务信息监听器计数管理器
 *
 * @author zai
 * 2020-07-20 18:48:11
 */
public class ServiceListenerCounterManager {
	
	
	protected Map<String, ServiceListenerCounter> serviceListenerCounters = new ConcurrentHashMap<>();
	
	/**
	 * 添加计数器
	 *
	 * @param counter
	 * @author zai
	 * 2020-07-20 18:48:27
	 */
	public void addCounter(ServiceListenerCounter counter) {
		ServiceListenerCounter old = this.serviceListenerCounters.put(counter.getServiceId(), counter);
		if (old == null) {
			counter.getSession().addDisconnectListener(s -> {
				this.serviceListenerCounters.remove(counter.getServiceId(), counter);
			});
		}
	}
	
	public Map<String, ServiceListenerCounter> getServiceListenerCounters() {
		return serviceListenerCounters;
	}
	
}
