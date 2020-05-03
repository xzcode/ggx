package com.ggx.router.client.service.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.message.Pack;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.group.RouterServiceGroup;

/**
 * 路由服务管理器
 *
 * @author zai 2020-05-03 04:00:16
 */
public class RouterServiceManager {

	/**
	 * 服务组集合Map<服务组id,服务组对象>
	 */
	protected final Map<String, RouterServiceGroup> serviceGroups = new ConcurrentHashMap<>();
	
	/**
	 * 添加服务
	 *
	 * @param service
	 * @author zai
	 * 2020-05-03 13:31:57
	 */
	public void addService(RouterService service) {
		RouterServiceGroup group = this.serviceGroups.get(service.getServiceId());
		if (group == null) {
			group = new RouterServiceGroup(service.getServiceId());
			
			RouterServiceGroup putIfAbsent = this.serviceGroups.putIfAbsent(group.getServiceGroupId(), group);
			if (putIfAbsent != null) {
				group = putIfAbsent;
			}
		}
		group.addService(service);
	}
	
	/**
	 * 获取服务组
	 *
	 * @param serviceGroupId
	 * @return
	 * @author zai
	 * 2020-05-03 13:47:11
	 */
	public RouterServiceGroup getServiceGroup(String serviceGroupId) {
		return this.serviceGroups.get(serviceGroupId);
	}
	
	/**
	 * 移除服务
	 *
	 * @param serviceGroupId
	 * @param serviceId
	 * @author zai
	 * 2020-05-03 14:09:46
	 */
	public void removeService(String serviceGroupId, String serviceId) {
		RouterServiceGroup group = this.serviceGroups.get(serviceGroupId);
		if (group != null) {
			group.reomoveService(serviceId);
		}
	}
	
	public Map<String, RouterServiceGroup> getServiceGroups() {
		return serviceGroups;
	}

}
