package com.ggx.admin.server.handler.registry.model.resp;

import java.util.List;

import com.ggx.docs.core.annotation.DocsModel;
import com.ggx.docs.core.annotation.DocsModelProperty;

@DocsModel(desc = "监听注册中心数据推送", actionId = RegistryDataResp.ACTION_ID)
public class RegistryDataResp {

	public static final String ACTION_ID = "registry.data.resp";

	@DocsModelProperty("服务列表")
	private List<ServiceInfoModel> serviceInfos;
	
	

	public RegistryDataResp() {
	}
	
	public RegistryDataResp(List<ServiceInfoModel> serviceInfos) {
		super();
		this.serviceInfos = serviceInfos;
	}


	public List<ServiceInfoModel> getServiceInfos() {
		return serviceInfos;
	}

	public void setServiceInfos(List<ServiceInfoModel> serviceInfos) {
		this.serviceInfos = serviceInfos;
	}
	
	

}
