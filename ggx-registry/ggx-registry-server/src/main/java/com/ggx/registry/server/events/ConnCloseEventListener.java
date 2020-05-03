package com.ggx.registry.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.registry.common.message.resp.RegistryServiceUnregisterResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.registry.server.constant.RegistryServerSessionKeys;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private RegistryServerConfig config;

	public ConnCloseEventListener(RegistryServerConfig config) {
		super();
		this.config = config;
	}

	public void setConfig(RegistryServerConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		//连接关闭.立即移除服务信息
		GGSession session = eventData.getSession();
		ServiceInfo serviceInfo = session.getAttribute(RegistryServerSessionKeys.SERVICE_INFO, ServiceInfo.class);
		if (serviceInfo == null) {
			return;
		}
		config.getServiceManager().removeService(serviceInfo);
		RegistryServiceUnregisterResp resp = new RegistryServiceUnregisterResp();
		resp.setServiceName(serviceInfo.getServiceGroupId());
		resp.setServiceId(serviceInfo.getServiceId());
		
		ServiceManager serviceManager = config.getServiceManager();
		serviceManager.sendToAllServices(resp);
		
		GGLoggerUtil.getLogger(this).warn("Service unregristry! serviceName: {}, serviceId: {}", serviceInfo.getServiceGroupId(), serviceInfo.getServiceId());
		
	}

	
}
