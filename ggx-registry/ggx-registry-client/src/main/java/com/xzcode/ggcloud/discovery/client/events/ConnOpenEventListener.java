package com.xzcode.ggcloud.discovery.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;
import com.xzcode.ggcloud.discovery.client.config.DiscoveryClientConfig;
import com.xzcode.ggcloud.discovery.common.message.req.DiscoveryServiceRegisterReq;
import com.xzcode.ggcloud.discovery.common.message.req.model.ServiceInfoModel;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;

public class ConnOpenEventListener implements EventListener<Void>{

	private DiscoveryClientConfig config;
	
	public ConnOpenEventListener(DiscoveryClientConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> e) {
		config.getRegistryManager().getRegistriedInfo().setActive(true);
		
		//打开连接，发送注册请求
		//发送注册请求
		GGSession session = e.getSession();
		config.setSession(session);
		
		DiscoveryServiceRegisterReq req = new DiscoveryServiceRegisterReq();
		req.setAuthToken(config.getAuthToken());
		ServiceInfo serviceInfo = new ServiceInfo();
		
		serviceInfo.setRegion(config.getRegion());
		serviceInfo.setZone(config.getZone());
		serviceInfo.setServiceId(config.getServiceId());
		serviceInfo.setServiceName(config.getServiceName());
		serviceInfo.setCustomData(config.getCustomData());
		
		req.setServiceInfo(serviceInfo);
		
		session.send(req);
	}

}
