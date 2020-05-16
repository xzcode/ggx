package com.ggx.router.client.service.group;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.ggx.router.client.service.RouterService;

/**
 * 路由服务组
 *
 * @author zai
 * 2020-05-03 03:05:36
 */
public class RouterServiceGroup {
	
	/**
	 * 服务组id
	 */
	protected String serviceGroupId;
	
	/**
	 * actionId前缀
	 */
	protected String actionIdPrefix;
	
	/**
	 * 服务集合Map<服务id, 服务对象>
	 */
	protected final Map<String, RouterService> services = new ConcurrentHashMap<>();
	
	/**
	 * 从小到大排列的路由服务列表
	 */
	protected final List<RouterService> sortedServiceList = new CopyOnWriteArrayList<RouterService>();
	

	public RouterServiceGroup(String serviceGroupId) {
		super();
		this.serviceGroupId = serviceGroupId;
	}
	
	
	public void addService(RouterService service) {
		this.services.put(service.getServiceId(), service);
		this.sortedServiceList.add(service);
	}
	
	
	public RouterService getService(String serviceId) {
		return this.services.get(serviceId);
	}
	
	
	/**
	 * 移除服务
	 *
	 * @param serviceId
	 * @author zai
	 * 2020-05-04 17:39:11
	 */
	public void reomoveService(String serviceId) {
		RouterService routerService = this.services.remove(serviceId);
		if (routerService != null) {
			//也从列表中移除
			this.sortedServiceList.remove(routerService);
			//重新排序列表
			this.sortServiceList();
			routerService.shutdown();
		}
	}
	
	/**
	 * 从大到小排列服务列表
	 *
	 * @author zai
	 * 2020-05-06 17:26:15
	 */
	private void sortServiceList() {
		this.sortedServiceList.sort((a, b) -> {
			return a.getLoad().get() - b.getLoad().get();
		});
	}
	
	/**
	 * 获取最低负载的路由服务
	 *
	 * @return
	 * @author zai
	 * 2020-05-06 17:27:52
	 */
	public RouterService getLowLoadingRouterService() {
		if (this.sortedServiceList.size() > 0) {
			return this.sortedServiceList.get(0);
		}
		return null;
	}
	
	/**
	 * 随机获取服务
	 *
	 * @return
	 * @author zai
	 * 2020-05-16 19:14:52
	 */
	public RouterService getRandomRouterService() {
		if (this.sortedServiceList.size() == 1) {
			return this.sortedServiceList.get(0);
		}
		if (this.sortedServiceList.size() > 1) {
			return this.sortedServiceList.get(ThreadLocalRandom.current().nextInt(this.sortedServiceList.size()));
		}
		return null;
	}
	
	public String getServiceGroupId() {
		return serviceGroupId;
	}
	
	public void setServiceGroupId(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}
	
	public Map<String, RouterService> getServices() {
		return services;
	}
	
	public String getActionIdPrefix() {
		return actionIdPrefix;
	}
	
	public void setActionIdPrefix(String actionIdPrefix) {
		this.actionIdPrefix = actionIdPrefix;
	}


}
