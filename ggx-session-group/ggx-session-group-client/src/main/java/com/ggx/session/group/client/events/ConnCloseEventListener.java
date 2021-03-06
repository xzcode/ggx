package com.ggx.session.group.client.events;

import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.session.group.client.config.SessionGroupClientConfig;

public class ConnCloseEventListener implements EventListener<Void>{
	
	private SessionGroupClientConfig config;
	
	public ConnCloseEventListener(SessionGroupClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void onEvent(EventData<Void> eventData) {
		
		SessionManager sessionManager = this.config.getServiceClient().getSessionManager();
		sessionManager.remove(eventData.getSession().getSessonId());
		
		//断开连接后，创建新连接
		this.config.getSessionGroupClient().connectOne(config.getServerHost(), config.getServerPort());
	}


	
}
