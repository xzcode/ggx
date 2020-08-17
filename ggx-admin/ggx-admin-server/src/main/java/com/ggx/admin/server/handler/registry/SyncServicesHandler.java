package com.ggx.admin.server.handler.registry;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.admin.collector.server.GGXAdminCollectorServer;
import com.ggx.admin.collector.server.service.ServiceDataService;
import com.ggx.admin.common.collector.data.model.service.ServiceData;
import com.ggx.admin.server.handler.registry.model.req.SyncServicesReq;
import com.ggx.admin.server.handler.registry.model.resp.SyncServicesResp;
import com.ggx.admin.server.handler.registry.model.resp.ServiceDataModel;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.spring.support.annotation.GGXMessageHandler;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 登录控制器
 * 
 * 
 * @author zai 2019-10-15 21:46:22
 */
@GGXMessageHandler
public class SyncServicesHandler implements MessageHandler<SyncServicesReq> {

	@Autowired
	private GGXAdminCollectorServer ggxAdminCollectorServer;


	@PostConstruct
	public void init() {
	}

	@Override
	public void handle(MessageData<SyncServicesReq> messageData) {
		GGSession session = messageData.getSession();
		ServiceDataService serviceDataService = ggxAdminCollectorServer.getConfig().getServiceDataService();
		List<ServiceData> dataList = serviceDataService.getDataList();

		List<ServiceDataModel> serviceModels = new ArrayList<ServiceDataModel>(serviceList.size());
		for (ServiceData serviceData : dataList) {
			ServiceDataModel serviceModel = ServiceDataModel.create(serviceData);
			serviceModels.add(serviceModel);
		}

		session.send(new SyncServicesResp(serviceModels));

	}

}
