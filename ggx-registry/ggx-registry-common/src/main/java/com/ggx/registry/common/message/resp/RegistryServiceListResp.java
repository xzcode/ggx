package com.ggx.registry.common.message.resp;

import java.util.List;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.core.common.message.model.Message;
import com.ggx.registry.common.constant.RegistryConstant;
import com.ggx.registry.common.service.ServiceInfo;

/**
 * 服务列表推送
 * 
 * @author zai
 * 2020-02-04 11:44:51
 */
public class RegistryServiceListResp extends AbstractMessage{
	
	/**
	 * 服务列表
	 */
	private List<ServiceInfo> serviceList;
	

	public RegistryServiceListResp() {
	}

	public RegistryServiceListResp(List<ServiceInfo> serviceList) {
		this.serviceList = serviceList;
	}

	public List<ServiceInfo> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<ServiceInfo> serviceList) {
		this.serviceList = serviceList;
	}
	
}
