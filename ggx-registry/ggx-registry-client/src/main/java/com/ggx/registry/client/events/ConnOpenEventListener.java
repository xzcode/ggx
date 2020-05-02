package com.ggx.registry.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.common.message.req.RegistryServiceRegisterReq;
import com.ggx.registry.common.message.req.model.ServiceInfoModel;
import com.ggx.registry.common.service.ServiceInfo;

public class ConnOpenEventListener implements EventListener<Void>{

	private RegistryClientConfig config;
	
	public ConnOpenEventListener(RegistryClientConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> e) {
		config.getRegistryManager().getRegistriedInfo().setActive(true);
		
		//打开连接，发送注册请求
		//发送注册请求
		GGSession session = e.getSession();
		config.setSession(session);
		
		RegistryServiceRegisterReq req = new RegistryServiceRegisterReq();
		req.setAuthToken(config.getAuthToken());
		ServiceInfo serviceInfo = new ServiceInfo();
		
		serviceInfo.setRegion(config.getRegion());
		serviceInfo.setZone(config.getZone());
		serviceInfo.setServiceId(config.getServiceId());
		serviceInfo.setServiceGroupId(config.getServiceGroupId());
		serviceInfo.setCustomData(config.getCustomData());
		
		req.setServiceInfo(serviceInfo);
		
		session.send(req);
	}

}
