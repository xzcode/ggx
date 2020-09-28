package com.ggx.router.client.service.manager.group;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.router.client.service.RouterService;
import com.ggx.router.client.service.listener.AddRouterServiceListener;
import com.ggx.router.client.service.listener.RemoveRouterServiceListener;
import com.ggx.router.client.service.loadblance.RouterServiceLoadblancer;

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
	
	/**
	 * 添加路由服务监听器
	 */
	protected List<AddRouterServiceListener> addRouterServiceListeners = new CopyOnWriteArrayList<>();
	
	/**
	 * 移除路由服务监听器
	 */
	protected List<RemoveRouterServiceListener> removeRouterServiceListeners = new CopyOnWriteArrayList<>();
	
	/**
	 * 路由服务负载均衡器
	 */
	protected RouterServiceLoadblancer routerServiceLoadblancer; 
	
	
	

	public RouterServiceGroup(String serviceGroupId, RouterServiceLoadblancer routerServiceLoadblancer) {
		super();
		this.serviceGroupId = serviceGroupId;
		this.routerServiceLoadblancer = routerServiceLoadblancer;
		this.routerServiceLoadblancer.setRouterServiceGroup(this);
	}
	
	
	public void addService(RouterService service) {
		RouterService old = this.services.put(service.getServiceId(), service);
		if (old == null) {
			this.sortedServiceList.add(service);			
			for (AddRouterServiceListener addRouterServiceListener : addRouterServiceListeners) {
				try {
					addRouterServiceListener.trigger(service);
				} catch (Exception e) {
					GGLoggerUtil.getLogger(this).error("AddRouterServiceListener Error!", e);
				}
			}
		}else {
			handleServiceAfterRemove(old);
		}
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
	public void removeService(String serviceId) {
		RouterService routerService = this.services.remove(serviceId);
		if (routerService != null) {
			handleServiceAfterRemove(routerService);
		}
	}
	
	/**
	 * 处理移除后的服务
	 *
	 * @param routerService
	 * @author zzz
	 * 2020-08-11 19:01:11
	 */
	private void handleServiceAfterRemove(RouterService routerService) {
		//也从列表中移除
		this.sortedServiceList.remove(routerService);
		//重新排序列表
		this.sortServiceList();
		routerService.shutdown();
		for (RemoveRouterServiceListener removeRouterServiceListener : removeRouterServiceListeners) {
			try {
				removeRouterServiceListener.trigger(routerService);
			} catch (Exception e) {
				GGLoggerUtil.getLogger(this).error("AddRouterServiceListener Error!", e);
			}
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
	 * 转发
	 *
	 * @param pack
	 * @return
	 * @author zai
	 * 2020-05-23 11:34:02
	 */
	public GGXFuture dispatch(Pack pack) {
		return this.routerServiceLoadblancer.dispatch(pack);
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
	
	public int size() {
		return this.services.size();
	}
	
	/**
	 * 随机转发
	 *
	 * @param pack
	 * @return
	 * @author zai
	 * 2020-05-22 18:24:40
	 */
	public GGXFuture dispatchRandom(Pack pack) {
		RouterService randomRouterService = this.getRandomRouterService();
		if (randomRouterService != null) {
			return randomRouterService.dispatch(pack);
		}
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}
	
	public void addAddRouterServiceListener(AddRouterServiceListener listener) {
		addRouterServiceListeners.add(listener);
	}

	public void addRemoveRouterServiceListener(RemoveRouterServiceListener listener) {
		removeRouterServiceListeners.add(listener);
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

	public RouterServiceLoadblancer getRouterServiceLoadblancer() {
		return routerServiceLoadblancer;
	}


	public void removeAllUnavaliableRouterServices() {
		RouterServiceGroup group = this;
		Map<String, RouterService> services = group.getServices();
		Set<Entry<String, RouterService>> servicesEntrySet = services.entrySet();
		for (Entry<String, RouterService> serviceEntry : servicesEntrySet) {
			RouterService service = serviceEntry.getValue();
			if (!service.isAvailable()) {
				//移除服务
				group.removeService(service.getServiceId());
				//关闭服务
				service.shutdown();
			}
		}
		
	}

}
