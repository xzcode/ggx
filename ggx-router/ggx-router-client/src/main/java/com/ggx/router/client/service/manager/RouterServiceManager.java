package com.ggx.router.client.service.manager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
		RouterServiceGroup group = this.serviceGroups.get(service.getServiceGroupId());
		if (group == null) {
			group = new RouterServiceGroup(service.getServiceGroupId());
			group.setActionIdPrefix(service.getActionIdPrefix());
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
	
	/**
	 * 获取服务
	 *
	 * @param serviceGroupId
	 * @param serviceId
	 * @return
	 * @author zai
	 * 2020-05-04 13:02:26
	 */
	public RouterService getService(String serviceGroupId, String serviceId) {
		RouterServiceGroup group = this.serviceGroups.get(serviceGroupId);
		if (group != null) {
			return group.getService(serviceId);
		}
		return null;
	}
	
	/**
	 * 获取服务组集合
	 *
	 * @return
	 * @author zai
	 * 2020-05-04 13:02:38
	 */
	public Map<String, RouterServiceGroup> getServiceGroups() {
		return serviceGroups;
	}
	
	public void removeAllUnavaliableRouterServices() {
		Set<Entry<String, RouterServiceGroup>> serviceGroupsEntrySet = this.serviceGroups.entrySet();
		
		for (Entry<String, RouterServiceGroup> serviceGroupsEntry : serviceGroupsEntrySet) {
			RouterServiceGroup group = serviceGroupsEntry.getValue();
			Map<String, RouterService> services = group.getServices();
			Set<Entry<String, RouterService>> servicesEntrySet = services.entrySet();
			for (Entry<String, RouterService> serviceEntry : servicesEntrySet) {
				RouterService service = serviceEntry.getValue();
				if (!service.isAvailable()) {
					//移除服务
					group.reomoveService(service.getServiceId());
					//关闭服务
					service.shutdown();
				}
			}
			
		}
		
	}

}
