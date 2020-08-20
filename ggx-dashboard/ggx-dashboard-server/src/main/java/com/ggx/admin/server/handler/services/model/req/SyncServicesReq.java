package com.ggx.admin.server.handler.services.model.req;

import com.ggx.core.common.message.model.AbstractMessage;

public class SyncServicesReq extends AbstractMessage{
	
	private String serviceId;
	
	
	public String getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	
}
