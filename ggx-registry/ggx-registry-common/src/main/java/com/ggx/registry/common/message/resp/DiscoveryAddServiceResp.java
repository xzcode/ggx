package com.ggx.registry.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 新增服务推送
 * 
 * @author zai
 * 2020-02-10 19:43:35
 */
public class DiscoveryAddServiceResp  implements Message{
	
	public static final String ACTION = "GG.DISCOVERY.ADD.SERVICE.RESP";
	
	@Override
	public String getActionId() {
		return ACTION;
	}
	
	/**
	 * 服务信息
	 */
	private ServiceInfo serviceInfo;
	

	public DiscoveryAddServiceResp() {
		
	}

	public DiscoveryAddServiceResp(ServiceInfo serviceInfo) {
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
