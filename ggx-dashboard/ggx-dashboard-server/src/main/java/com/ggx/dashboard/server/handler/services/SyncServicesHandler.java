package com.ggx.dashboard.server.handler.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.admin.collector.server.GGXDashboardCollectorServer;
import com.ggx.admin.collector.server.service.ServiceDataService;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.dashboard.common.collector.data.model.service.ServiceData;
import com.ggx.dashboard.server.handler.services.model.req.SyncServicesReq;
import com.ggx.dashboard.server.handler.services.model.resp.ServiceDataModel;
import com.ggx.dashboard.server.handler.services.model.resp.SyncServicesResp;
import com.ggx.server.spring.boot.starter.annotation.GGXController;

/**
 * 登录控制器
 * 
 * 
 * @author zai 2019-10-15 21:46:22
 */
@GGXController
public class SyncServicesHandler {

	@Autowired
	private GGXDashboardCollectorServer ggxAdminCollectorServer;
	
	private ServiceDataService serviceDataService;


	@PostConstruct
	public void init() {
	}

	@GGXAction
	public void handle(SyncServicesReq req, GGXSession session) {
		List<ServiceData> dataList = serviceDataService.getDataList();
		List<ServiceDataModel> serviceModels = new ArrayList<ServiceDataModel>(dataList.size());
		for (ServiceData serviceData : dataList) {
			ServiceDataModel serviceModel = ServiceDataModel.create(serviceData);
			serviceModels.add(serviceModel);
		}

		session.send(new SyncServicesResp(serviceModels));

	}

}
