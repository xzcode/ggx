package com.ggx.router.server.handler.req;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.message.request.task.MessageDataTask;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.group.server.session.GroupServiceServerSession;
import com.ggx.router.client.RouterClient;
import com.ggx.router.common.message.req.RouterMessageReq;
import com.ggx.router.server.config.RouterServerConfig;
import com.ggx.router.server.session.RouterServiceServerSession;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

/**
 * 路由消息请求
 *
 * @author zai
 * 2020-05-11 14:30:41
 */
public class RouterMessageReqHandler implements MessageDataHandler<RouterMessageReq>{
	
	private RouterServerConfig config;
	

	public RouterMessageReqHandler(RouterServerConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<RouterMessageReq> messageData) {
		GGSession session = messageData.getSession();
		RouterMessageReq req = messageData.getMessage();
		byte[] routeActionBytes = req.getAction();
		byte[] routeMessageBytes = req.getMessage();
		
		if (this.config.isEnableServiceServer()) {
			GGServer serviceServer = this.config.getServiceServer();
			GGServerConfig serviceServerConfig = serviceServer.getConfig();
			if (serviceServer != null) {
				//获取传递的sessionid
				String routeSessionId = req.getRouteSessionId();
				
				if (routeSessionId != null) {
					SessionManager sessionManager = serviceServerConfig.getSessionManager();
					
					//创建路由业务服务端session
					RouterServiceServerSession serviceSession = (RouterServiceServerSession) sessionManager.getSession(routeSessionId);
					if (serviceSession == null) {
						serviceSession = new RouterServiceServerSession((GroupServiceServerSession) messageData.getSession(), req.getRouteSessionId(), serviceServerConfig);
						GGSession addSessionIfAbsent = sessionManager.addSessionIfAbsent(serviceSession);
						if (addSessionIfAbsent != null) {
							serviceSession = (RouterServiceServerSession) addSessionIfAbsent;
						}
					}
					
					//提交任务到业务服务端
					Pack pack = new Pack(serviceSession, req.getAction(), req.getMessage());
					serviceServer.submitTask(new MessageDataTask(pack , serviceServerConfig));
					
				}
			}
		}
		
		if (this.config.isEnableForwardRouterClient()) {
		
			RouterClient forwardRouterClient = this.config.getForwardRouterClient();
			if (forwardRouterClient != null) {
				forwardRouterClient.route(new Pack(session, routeActionBytes, routeMessageBytes));
			}
		
		}
		
	}
	

}
