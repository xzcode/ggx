package com.xzcode.ggcloud.discovery.common.message.resp;

import com.ggx.core.common.message.model.IMessage;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;

/**
 * 服务更新推送
 * 
 * @author zai
 * 2020-02-04 11:34:37
 */
public class DiscoveryServiceUpdateResp  implements IMessage{
	
	public static final String ACTION = "GG.DISCOVERY.SERVICE.UPDATE.RESP";
	
	@Override
	public String getActionId() {
		return ACTION;
	}
	
	/**
	 * 服务信息
	 */
	private ServiceInfo serviceInfo;
	

	public DiscoveryServiceUpdateResp() {
		
	}

	public DiscoveryServiceUpdateResp(ServiceInfo serviceInfo) {
		super();
		this.serviceInfo = serviceInfo;
	}


	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}
	
	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	
}
