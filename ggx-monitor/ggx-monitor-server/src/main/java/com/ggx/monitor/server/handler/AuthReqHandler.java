package com.ggx.monitor.server.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.monitor.common.message.req.AuthReq;
import com.ggx.monitor.common.message.resp.AuthResp;
import com.ggx.monitor.server.config.GameMonitorServerConfig;
import com.ggx.monitor.server.constant.GameMonitorServerSessionKeys;

/**
 * 客户端认证处理
 *
 * @author zai
 * 2020-04-26 10:35:45
 */
public class AuthReqHandler implements MessageHandler<AuthReq>{
	
	private GameMonitorServerConfig config;
	

	public AuthReqHandler(GameMonitorServerConfig config) {
		super();
		this.config = config;
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
				session.addAttribute(GameMonitorServerSessionKeys.IS_AUTHED, true);
				session.send(new AuthResp(true));
				return;
			}
		}
		session.send(new AuthResp(false, "Auth Token Is Incorrect"));
	}


	

}
