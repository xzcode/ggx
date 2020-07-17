package com.ggx.admin.server.handler.service.model.req;

import com.ggx.core.common.message.model.AbstractMessage;

public class GetServiceInfoReq extends AbstractMessage{
	
	private String serviceId;
	
	
	public String getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	
}
