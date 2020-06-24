package com.ggx.monitor.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.monitor.common.constant.GameMonitorConstant;
import com.ggx.monitor.common.data.model.service.ServiceData;

/**
 * 游戏数据更新请求
 *
 * @author zai 2020-04-23 12:11:20
 */
public class ServiceDataReq implements Message {

	public static final String ACTION = GameMonitorConstant.ACTION_ID_PREFIX + "SERVICE.DATA.REQ";

	@Override
	public String getActionId() {
		return ACTION;
	}

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
