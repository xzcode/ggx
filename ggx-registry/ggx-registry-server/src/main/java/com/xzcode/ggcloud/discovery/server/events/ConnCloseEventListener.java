package com.xzcode.ggcloud.discovery.server.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceUnregisterResp;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;
import com.xzcode.ggcloud.discovery.common.service.ServiceManager;
import com.xzcode.ggcloud.discovery.server.config.DiscoveryServerConfig;
import com.xzcode.ggcloud.discovery.server.constant.DiscoveryServerSessionKeys;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private DiscoveryServerConfig config;

	public ConnCloseEventListener(DiscoveryServerConfig config) {
		super();
		this.config = config;
	}

	public void setConfig(DiscoveryServerConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		//连接关闭.立即移除服务信息
		GGSession session = eventData.getSession();
		ServiceInfo serviceInfo = session.getAttribute(DiscoveryServerSessionKeys.SERVICE_INFO, ServiceInfo.class);
		if (serviceInfo == null) {
			return;
		}
		config.getServiceManager().removeService(serviceInfo);
		DiscoveryServiceUnregisterResp resp = new DiscoveryServiceUnregisterResp();
		resp.setServiceName(serviceInfo.getServiceName());
		resp.setServiceId(serviceInfo.getServiceId());
		
		ServiceManager serviceManager = config.getServiceManager();
		serviceManager.sendToAllServices(resp);
		
		GGLoggerUtil.getLogger(this).warn("Service unregristry! serviceName: {}, serviceId: {}", serviceInfo.getServiceName(), serviceInfo.getServiceId());
		
	}

	
}
