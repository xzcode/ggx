package com.ggx.registry.common.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.common.service.listener.RegisterServiceListener;
import com.ggx.registry.common.service.listener.UnregisterServiceListener;
import com.ggx.registry.common.service.listener.UpdateServiceListener;

/**
 * 服务管理器
 * 
 * @author zai
 * 2020-02-04 14:33:19
 */
public class ServiceManager {
	
	/**
	 * 服务组集合
	 */
	private Map<String, ServiceGroup> serviceGroups = new ConcurrentHashMap<>();
	
	/**
	 * 注册监听器
	 */
	private List<RegisterServiceListener> registerListeners = new CopyOnWriteArrayList<>();
	
	/**
	 * 取消注册监听器
	 */
	private List<UnregisterServiceListener> unregisterListeners = new CopyOnWriteArrayList<>();
	
	/**
	 * 服务更新监听器
	 */
	private List<UpdateServiceListener> updateListeners = new CopyOnWriteArrayList<>();
	
	
	/**
	 * 添加注册监听器
	 * 
	 * @param listener
	 * @author zai
	 * 2020-02-06 15:15:56
	 */
	public void addRegisterListener(RegisterServiceListener listener) {
		this.registerListeners.add(listener);
	}
	
	/**
	 * 添加取消注册监听器
	 * 
	 * @param listener
	 * @author zai
	 * 2020-02-06 15:16:06
	 */
	public void addUnregisterListener(UnregisterServiceListener listener) {
		this.unregisterListeners.add(listener);
	}
	
	/**
	 * 添加更新服务监听器
	 * 
	 * @param listener
	 * @author zai
	 * 2020-02-06 15:16:06
	 */
	public void addUpdateListener(UpdateServiceListener listener) {
		this.updateListeners.add(listener);
	}
	
	/**
	 * 注册服务
	 * 
	 * @param service
	 * @author zai
	 * 2020-02-04 14:33:41
	 */
	public void registerService(ServiceInfo service) {
		ServiceGroup group = serviceGroups.get(service.getServiceGroupId());
		if (group == null) {
				group = new ServiceGroup(service.getServiceGroupId());
				ServiceGroup putIfAbsent = serviceGroups.putIfAbsent(service.getServiceGroupId(), group);
				if (putIfAbsent != null) {
					group = putIfAbsent;
				}
		}
		
		group.addServiceInfo(service);
		if (this.registerListeners != null) {
			for (RegisterServiceListener listener : registerListeners) {
				listener.onRegister(service);						
			}
		}
		
	}
	
	/**
	 * 移除服务
	 * 
	 * @param discoveryClientServiceInfo
	 * @author zai
	 * 2020-02-04 14:33:48
	 */
	public void removeService(ServiceInfo service) {
		ServiceGroup groups = serviceGroups.get(service.getServiceGroupId());
		if (groups != null) {
			groups.removeServiceInfo(service.getServiceId());
			if (this.unregisterListeners != null) {
				for (UnregisterServiceListener listener : unregisterListeners) {
					listener.onUnregister(service);						
				}
			}
		}
	}
	
	/**
	 * 更新服务
	 * 
	 * @param service
	 * @author zai
	 * 2020-02-06 17:22:06
	 */
	public void updateService(ServiceInfo service) {
		ServiceGroup group = serviceGroups.get(service.getServiceGroupId());
		if (group != null) {
			ServiceInfo oldService = group.getServiceInfo(service.getServiceId());
			if (oldService != null) {
				oldService.setCustomData(service.getCustomData());
				
				if (this.updateListeners != null) {
					for (UpdateServiceListener listener : updateListeners) {
						listener.onUpdate(service);						
					}
				}
			}
		}
	}
	
	/**
	 * 移除服务
	 * 
	 * @param serviceName
	 * @param serviceId
	 * @author zai
	 * 2020-02-06 15:01:25
	 */
	public void removeService(String serviceName, String serviceId) {
		ServiceGroup groups = serviceGroups.get(serviceName);
		if (groups != null) {
			ServiceInfo service = groups.removeServiceInfo(serviceId);
			if (service == null) {
				return;
			}
			if (this.unregisterListeners != null) {
				for (UnregisterServiceListener listener : unregisterListeners) {
					listener.onUnregister(service);						
				}
			}
		}
		
	}
	
	/**
	 * 获取服务组
	 * 
	 * @param serviceName
	 * @return
	 * @author zai
	 * 2020-02-04 14:33:57
	 */
	public ServiceGroup getServiceGroup(String serviceName) {
		return serviceGroups.get(serviceName);
	}
	
	/**
	 * 获取服务
	 * 
	 * @param serviceId
	 * @return
	 * @author zai
	 * 2020-02-04 14:34:10
	 */
	public ServiceInfo getService(String serviceId) {
		for (Entry<String, ServiceGroup> entry : serviceGroups.entrySet()) {
			ServiceGroup group = entry.getValue();
			ServiceInfo service = group.getServiceInfo(serviceId);
			if (service != null) {
				return service;
			}
		}
		return null;
	}
	
	/**
	 * 获取服务列表
	 * 
	 * @return
	 * @author zai
	 * 2020-02-04 18:53:42
	 */
	public List<ServiceInfo> getServiceList() {
		List<ServiceInfo> list = new ArrayList<>();
		
		for (Entry<String, ServiceGroup> groupEntry : serviceGroups.entrySet()) {
			
			ServiceGroup serviceGroup = groupEntry.getValue();
			Map<String, ServiceInfo> services = serviceGroup.getServices();
			Iterator<Entry<String, ServiceInfo>> serviceIterator = services.entrySet().iterator();
			
			while (serviceIterator.hasNext()) {
				ServiceInfo serviceInfo = (ServiceInfo) serviceIterator.next().getValue();
				list.add(serviceInfo);
			}
		}
		return list;
	}
	
	/**
	 * 获取服务组列表
	 *
	 * @return
	 * @author zai
	 * 2020-05-18 15:38:25
	 */
	public List<ServiceGroup> getServiceGroupList() {
		List<ServiceGroup> list = new ArrayList<>();
		
		for (Entry<String, ServiceGroup> groupEntry : serviceGroups.entrySet()) {
			
			ServiceGroup serviceGroup = groupEntry.getValue();
			list.add(serviceGroup);
		}
		return list;
	}
	
	public void clearAllServices() {
		serviceGroups.clear();
	}
	
	/**
	 * 发送消息给所有服务
	 * 
	 * @param message
	 * @author zai
	 * 2020-02-13 09:38:54
	 */
	public void sendToAllServices(Message message) {
		List<ServiceInfo> serviceList = getServiceList();
		for (ServiceInfo info : serviceList) {
			GGSession infoSession = info.getSession();
			if (infoSession != null) {
				infoSession.send(message);
			}
		}
	}
	
}
