package com.ggx.registry.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.registry.server.constant.RegistryServerSessionKeys;

public class ConnHeartbeatLostEventListener implements EventListener<Void>{
	
	private RegistryServerConfig config;

	public ConnHeartbeatLostEventListener(RegistryServerConfig config) {
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
		
		GGLoggerUtil.getLogger(this).warn("Service heart beat lost! serviceName: {}, serviceId: {}", serviceInfo.getServiceName(), serviceInfo.getServiceId());
		
	}

	
}
