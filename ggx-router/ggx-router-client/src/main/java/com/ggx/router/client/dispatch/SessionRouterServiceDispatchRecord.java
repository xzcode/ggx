package com.ggx.router.client.dispatch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.router.client.service.RouterService;

/**
 * 会话路由转发记录
 *
 * @author zai
 * 2020-05-20 12:26:00
 */
public class SessionRouterServiceDispatchRecord {
	
	/**
	 * 会话id
	 */
	protected String sessionId;
	
	/**
	 * 服务集合Map<服务id,服务对象>
	 */
	protected final Map<String ,RouterService> services = new ConcurrentHashMap<>(8);
	
	protected final List<RouterService> serviceList = new CopyOnWriteArrayList<>();
	
	
	public SessionRouterServiceDispatchRecord(String sessionId) {
		super();
		this.sessionId = sessionId;
	}

	public List<RouterService> getServices() {
		return serviceList;
	}
	
	public RouterService addServiceIfAbsent(RouterService service) {
		RouterService oldService = services.putIfAbsent(service.getServiceId(), service);
		if (oldService == null) {
			service.addShutdownListener(s -> {
				removeService(service);
			});
			serviceList.add(service);
		}
		return oldService;
	}
	
	public void removeService(RouterService routerService) {
		this.services.remove(routerService.getServiceId());
		serviceList.remove(routerService);
	}
	
	public RouterService getService(String serviceId) {
		return services.get(serviceId);
	}

}
