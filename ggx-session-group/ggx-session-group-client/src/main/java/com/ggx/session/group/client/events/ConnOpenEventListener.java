package com.ggx.session.group.client.events;

import com.ggx.core.client.config.GGClientConfig;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.common.message.req.AuthReq;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.ggx.session.group.client.session.GroupServiceClientSession;

/**
 * 连接打开事件监听
 *
 * @author zai
 * 2020-04-08 11:23:13
 */
public class ConnOpenEventListener implements EventListener<Void>{

	private SessionGroupClientConfig config;
	
	public ConnOpenEventListener(SessionGroupClientConfig config) {
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> e) {
		//打开连接，发送认证
		GGSession groupSession = e.getSession();
		groupSession.send(new AuthReq(config.getAuthToken()));
		
		GGSessionGroupManager sessionGroupManager = this.config.getSessionGroupManager();
		sessionGroupManager.addSession(this.config.getSessionGroupId(), groupSession);
		
		
		if (this.config.isEnableServiceClient()) {
			
			GGClientConfig serviceClientConfig = this.config.getServiceClient().getConfig();
			SessionManager sessionManager = serviceClientConfig.getSessionManager();
			
			GroupServiceClientSession serviceServerSession = new GroupServiceClientSession(groupSession.getSessonId(), this.config.getSessionGroupId(), sessionGroupManager, serviceClientConfig);
			GGSession addSessionIfAbsent = sessionManager.addSessionIfAbsent(serviceServerSession);
			if (addSessionIfAbsent != null) {
				serviceServerSession = (GroupServiceClientSession) addSessionIfAbsent;
			}
			sessionManager.addSessionIfAbsent(serviceServerSession);
		}
		
	}

}
