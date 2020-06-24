package com.ggx.monitor.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;
import com.ggx.monitor.client.config.GameMonitorClientConfig;
import com.ggx.monitor.common.message.req.AuthReq;

public class ConnOpenEventListener implements EventListener<Void>{

	private GameMonitorClientConfig config;
	
	public ConnOpenEventListener(GameMonitorClientConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> e) {
		
		//打开连接，发送注册请求
		//发送注册请求
		GGSession session = e.getSession();
		config.setSession(session);
		
		AuthReq req = new AuthReq();
		req.setAuthToken(config.getAuthToken());
		session.send(req);
	}

}
