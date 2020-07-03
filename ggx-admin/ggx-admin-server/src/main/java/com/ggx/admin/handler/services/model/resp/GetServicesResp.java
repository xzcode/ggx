package com.ggx.admin.handler.services.model.resp;

import java.util.List;

import com.ggx.admin.common.collector.data.model.service.ServiceData;
import com.ggx.core.common.message.model.AbstractMessage;

public class GetServicesResp extends AbstractMessage{

	
	private List<ServiceData> services;
	
	
	public GetServicesResp() {
	}

	public GetServicesResp(List<ServiceData> services) {
		this.services = services;
	}

	public void setServices(List<ServiceData> services) {
		this.services = services;
	}
	
	public List<ServiceData> getServices() {
		return services;
	}

	

	
}
