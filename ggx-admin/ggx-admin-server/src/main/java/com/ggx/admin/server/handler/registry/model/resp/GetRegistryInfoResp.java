package com.ggx.admin.server.handler.registry.model.resp;

import java.util.List;

import com.ggx.core.common.message.model.AbstractMessage;

public class GetRegistryInfoResp extends AbstractMessage{

	
	private List<ServiceModel> services;
	
	
	public GetRegistryInfoResp() {
	}

	public GetRegistryInfoResp(List<ServiceModel> services) {
		this.services = services;
	}

	public void setServices(List<ServiceModel> services) {
		this.services = services;
	}
	
	public List<ServiceModel> getServices() {
		return services;
	}

	

	
}
