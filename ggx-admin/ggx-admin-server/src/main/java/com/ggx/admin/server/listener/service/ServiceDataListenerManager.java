package com.ggx.admin.server.listener.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ggx.admin.collector.server.GGXAdminCollectorServer;
import com.ggx.admin.collector.server.service.ServiceDataService;
import com.ggx.admin.common.collector.data.model.service.ServiceData;
import com.ggx.admin.server.handler.registry.model.resp.SyncServicesResp;
import com.ggx.admin.server.handler.registry.model.resp.ServiceDataModel;
import com.ggx.admin.server.listener.basic.BasicSessionListenerManager;
import com.ggx.admin.server.model.ServiceInfoSessionListener;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 注册中心信息监听管理器
 *
 * @author zai 2020-07-17 17:28:04
 */
@Component
public class ServiceDataListenerManager extends BasicSessionListenerManager {

	private ServiceDataService serviceDataService;


	@Autowired
	private GGXAdminCollectorServer ggxAdminCollectorServer;

	@PostConstruct
	public void init() {
		
		serviceDataService.addData(serviceId, data);
		
		this.serviceManager = registryClient.getConfig().getServiceManager();
		
		this.serviceManager.addRegisterListener((e) -> {
			sendRegistryToAllListeners();
		});
		this.serviceManager.addUnregisterListener((e) -> {
			sendRegistryToAllListeners();
		});
		this.serviceManager.addUnregisterListener((e) -> {
			sendRegistryToAllListeners();
		});
	}

	/**
	 * 获取服务集合
	 *
	 * @return
	 * @author zai 2020-07-20 14:20:50
	 */
	public List<ServiceDataModel> getServiceModels() {
		List<ServiceData> serviceDataList = serviceDataService.getDataList();

		List<ServiceDataModel> serviceModels = new ArrayList<ServiceDataModel>(serviceDataList.size());
		for (ServiceData serviceData : serviceDataList) {
			ServiceDataModel serviceModel = ServiceDataModel.create(serviceData);
			serviceModels.add(serviceModel);
		}
		return serviceModels;
	}

	/**
	 * 发送注册中心信息给所有监听者
	 *
	 * @author zai 2020-07-20 14:17:56
	 */
	public void sendRegistryToAllListeners() {

		List<ServiceDataModel> serviceModels = getServiceModels();

		for (Entry<GGSession, ServiceInfoSessionListener> entry : sessionListenrs.entrySet()) {
			entry.getValue().getSession().send(new SyncServicesResp(serviceModels));
		}
	}

	/**
	 * 发送注册中心信息给指定监听者
	 *
	 * @param listener
	 * @author zai 2020-07-20 14:22:40
	 */
	public void sendRegistryToListener(ServiceInfoSessionListener listener) {
		List<ServiceDataModel> serviceModels = getServiceModels();
		listener.getSession().send(new SyncServicesResp(serviceModels));

	}

}
