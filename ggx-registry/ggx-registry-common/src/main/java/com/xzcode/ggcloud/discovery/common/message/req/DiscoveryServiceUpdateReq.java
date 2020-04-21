package com.xzcode.ggcloud.discovery.common.message.req;

import com.ggx.core.common.message.model.IMessage;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;

/**
 * 服务更新请求
 * 
 * @author zai
 * 2020-02-04 15:30:56
 */
public class DiscoveryServiceUpdateReq implements IMessage{
	
	public static final String ACTION = "GG.DISCOVERY.SERVICE.UPDATE.REQ";
	
	@Override
	public String getActionId() {
		return ACTION;
	}
	
	//服务信息
	private ServiceInfo serviceInfo;

	public DiscoveryServiceUpdateReq() {
		
	}

	public DiscoveryServiceUpdateReq(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	
}
