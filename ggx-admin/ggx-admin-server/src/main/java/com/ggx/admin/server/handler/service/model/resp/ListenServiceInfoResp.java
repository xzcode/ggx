package com.ggx.admin.server.handler.service.model.resp;

import com.ggx.admin.server.handler.registry.model.resp.ServerDataModel;
import com.ggx.admin.server.handler.registry.model.resp.ServiceDataModel;
import com.ggx.core.common.message.model.AbstractMessage;

public class ListenServiceInfoResp extends AbstractMessage {

	private ServiceDataModel serviceData;

	private ServerDataModel serverData;

	public ListenServiceInfoResp() {
	}

	public ListenServiceInfoResp(ServiceDataModel serviceData, ServerDataModel serverData) {
		super();
		this.serviceData = serviceData;
		this.serverData = serverData;
	}

	public ServiceDataModel getServiceData() {
		return serviceData;
	}

	public void setServiceData(ServiceDataModel serviceData) {
		this.serviceData = serviceData;
	}

	public ServerDataModel getServerData() {
		return serverData;
	}

	public void setServerData(ServerDataModel serverData) {
		this.serverData = serverData;
	}

}
