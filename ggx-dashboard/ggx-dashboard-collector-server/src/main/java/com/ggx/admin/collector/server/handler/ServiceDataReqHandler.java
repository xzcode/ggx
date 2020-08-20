package com.ggx.admin.collector.server.handler;

import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.admin.collector.server.service.ServiceDataService;
import com.ggx.admin.common.collector.data.model.service.ServiceData;
import com.ggx.admin.common.collector.message.req.ServiceDataReq;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;

/**
 * 服务数据请求处理器
 *
 * @author zai 2020-06-24 18:15:42
 */
public class ServiceDataReqHandler implements MessageHandler<ServiceDataReq> {

	private GGXAdminCollectorServerConfig config;

	private ServiceDataService serviceDataService;

	public ServiceDataReqHandler(GGXAdminCollectorServerConfig config) {
		this.config = config;
		this.serviceDataService = config.getServiceDataService();
	}

	@Override
	public void handle(MessageData<ServiceDataReq> messageData) {
		ServiceDataReq req = messageData.getMessage();
		ServiceData data = req.getServiceData();
		serviceDataService.addData(data.getServiceId(), data);
	}

}
