package com.ggx.dashboard.common.collector.message.req;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.dashboard.common.collector.data.model.service.ServiceData;


public class ServiceDataReq  extends AbstractMessage {


	// 服务信息
	private ServiceData serviceData;
	
	public ServiceDataReq() {

	}

	public ServiceDataReq(ServiceData serviceData) {
		this.serviceData = serviceData;
	}

	public ServiceData getServiceData() {
		return serviceData;
	}

	public void setServiceData(ServiceData serviceData) {
		this.serviceData = serviceData;
	}
}
