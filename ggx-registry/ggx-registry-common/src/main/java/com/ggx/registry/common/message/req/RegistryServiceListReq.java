package com.ggx.registry.common.message.req;

import com.ggx.core.common.message.model.AbstractMessage;

public class RegistryServiceListReq  extends AbstractMessage{
	
	public static final RegistryServiceListReq ALL_SERVICE_INSTANT = new RegistryServiceListReq(ServiceTypeConstant.ALL_SERVICE);
	public static final RegistryServiceListReq COMPONENT_SERVICES_INSTANT = new RegistryServiceListReq(ServiceTypeConstant.COMPONENT_SERVICES);
	
	//服务类型
	private int serviceType;
	
	public RegistryServiceListReq() {
	}
	
	public RegistryServiceListReq(int serviceType) {
		super();
		this.serviceType = serviceType;
	}


	public static interface ServiceTypeConstant {
		int ALL_SERVICE = 1;
		int COMPONENT_SERVICES = 2;
	}
	

	public int getServiceType() {
		return serviceType;
	}


	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}
	
	
	
}
