package com.ggx.admin.collector.client.events;

import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.common.collector.message.req.AuthReq;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;

public class ConnOpenEventListener implements EventListener<Void>{

	private GGXAdminCollectorClientConfig config;
	
	public ConnOpenEventListener(GGXAdminCollectorClientConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> e) {
		
		//打开连接，发送注册请求
		//发送注册请求
		GGSession session = e.getSession();
		config.setSession(session);
		
		AuthReq req = new AuthReq();
		req.setAuthToken(config.getAuthToken(), config.getRegistryClient().getServiceId());
		session.send(req);
		
	}

}
