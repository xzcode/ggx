package com.ggx.admin.server.handler.registry.model.resp;

import java.util.List;

import com.ggx.core.common.message.model.AbstractMessage;

public class SyncServicesResp extends AbstractMessage{

	
	private List<ServiceDataModel> services;
	
	
	public SyncServicesResp() {
	}

	public SyncServicesResp(List<ServiceDataModel> services) {
		this.services = services;
	}

	public void setServices(List<ServiceDataModel> services) {
		this.services = services;
	}
	
	public List<ServiceDataModel> getServices() {
		return services;
	}

	

	
}
