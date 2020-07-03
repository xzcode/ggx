package com.ggx.admin.common.collector.message.req;

import com.ggx.admin.common.collector.data.model.service.ServiceData;
import com.ggx.core.common.message.model.AbstractMessage;

/**
 * 游戏数据更新请求
 *
 * @author zai 2020-04-23 12:11:20
 */
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
