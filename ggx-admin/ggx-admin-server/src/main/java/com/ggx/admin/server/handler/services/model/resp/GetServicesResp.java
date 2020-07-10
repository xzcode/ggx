package com.ggx.admin.server.handler.services.model.resp;

import java.util.List;

import com.ggx.core.common.message.model.AbstractMessage;

public class GetServicesResp extends AbstractMessage{

	
	private List<ServiceModel> services;
	
	
	public GetServicesResp() {
	}

	public GetServicesResp(List<ServiceModel> services) {
		this.services = services;
	}

	public void setServices(List<ServiceModel> services) {
		this.services = services;
	}
	
	public List<ServiceModel> getServices() {
		return services;
	}

	

	
}
