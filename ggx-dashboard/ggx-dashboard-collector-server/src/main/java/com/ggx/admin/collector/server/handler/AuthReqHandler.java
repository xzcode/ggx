package com.ggx.admin.collector.server.handler;

import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.admin.collector.server.constant.GGXAdminCollectorServerEvents;
import com.ggx.admin.collector.server.constant.GGXAdminCollectorServerSessionKeys;
import com.ggx.admin.collector.server.session.ServiceIdSessionManager;
import com.ggx.admin.common.collector.message.req.AuthReq;
import com.ggx.admin.common.collector.message.resp.AuthResp;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;

/**
 * 客户端认证处理
 *
 * @author zai
 * 2020-04-26 10:35:45
 */
public class AuthReqHandler implements MessageHandler<AuthReq>{
	
	private GGXAdminCollectorServerConfig config;
	private ServiceIdSessionManager serviceIdSessionManager ;
	

	public AuthReqHandler(GGXAdminCollectorServerConfig config) {
		super();
		this.config = config;
		this.serviceIdSessionManager = this.config.getServiceIdSessionManager();
	}
	


	@Override
	public void handle(MessageData<AuthReq> request) {
		GGSession session = request.getSession();
		AuthReq req = request.getMessage();
		String serverAuthToken = config.getAuthToken();
		//判断认证token是否正确
		if (serverAuthToken != null && !serverAuthToken.isEmpty()) {
			String clientAuthToken = req.getAuthToken();
			if (clientAuthToken != null && !clientAuthToken.isEmpty() && clientAuthToken.equals(serverAuthToken)) {
				session.addAttribute(GGXAdminCollectorServerSessionKeys.IS_AUTHED, true);
				session.addAttribute(GGXAdminCollectorServerSessionKeys.SERVICE_ID, req.getServiceId());
				
				//添加到serviceId与session映射集合
				this.serviceIdSessionManager.addSession(req.getServiceId(), session);
				
				//发射认证成功事件
				session.emitEvent(new EventData<Void>(session, GGXAdminCollectorServerEvents.AUTH_SUCCESS));
				session.send(new AuthResp(true));
				return;
			}
		}
		session.send(new AuthResp(false, "Auth Token Is Incorrect"));
	}


	

}
