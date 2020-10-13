package com.ggx.group.server.handler;

import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.message.receive.task.ReceiveMessageTask;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.group.common.message.req.DataTransferReq;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.group.server.constant.SessionGroupServerSessionKeys;
import com.ggx.group.server.session.GroupServiceServerSession;
import com.ggx.group.server.transfer.custom.CustomDataTransferHandler;

/**
 * 客户端认证请求
 *
 * @author zai 2020-04-07 10:57:11
 */
public class DataTransferReqHandler  {

	private SessionGroupServerConfig config;

	public DataTransferReqHandler(SessionGroupServerConfig config) {
		super();
		this.config = config;
	}

	@GGXAction
	public void handle(DataTransferReq req, GGXSession groupSession) {
		String groupSessionId = groupSession.getSessionId();
		
		GGXCoreServer serviceServer = config.getServiceServer();
		GGXCoreServerConfig serviceServerConfig = serviceServer.getConfig();
		
		//判断是否开启自定义传输数据处理器
		if (this.config.isEnableCustomDataTransferHandler() && this.config.getCustomDataTransferHandler() != null) {
			CustomDataTransferHandler customDataTransferHandler = this.config.getCustomDataTransferHandler();
			customDataTransferHandler.handle(req, groupSession);
			return;//开启自定义处理后，不再进行后续处理
		}
			
		SessionManager serviceSessionManager = serviceServerConfig.getSessionManager();
		//创建业务服务端session
		GroupServiceServerSession serviceSession = (GroupServiceServerSession) serviceSessionManager.getSession(groupSessionId);
		if (serviceSession == null) {
			String groupId = groupSession.getAttribute(SessionGroupServerSessionKeys.GROUP_SESSION_GROUP_ID, String.class);
			serviceSession = new GroupServiceServerSession(groupSessionId, groupId, config.getSessionGroupManager(), serviceServerConfig);
			GGXSession addSessionIfAbsent = serviceSessionManager.addSessionIfAbsent(serviceSession);
			if (addSessionIfAbsent != null) {
				serviceSession = (GroupServiceServerSession) addSessionIfAbsent;
			}else {
				String sessionId = serviceSession.getSessionId();
				serviceSession.addDisconnectListener(se -> {
					serviceSessionManager.remove(sessionId);
				});
			}
		}
		
		new ReceiveMessageTask(new Pack(serviceSession, req.getAction(), req.getMessage()), serviceServerConfig).run();
		
			
	}

}
