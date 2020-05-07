package com.ggx.group.server.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.message.request.task.MessageDataTask;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.group.common.message.req.DataTransferReq;
import com.ggx.group.server.config.SessionGroupServerConfig;
import com.ggx.group.server.constant.SessionGroupServerSessionKeys;
import com.ggx.group.server.session.ServiceServerSession;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

/**
 * 客户端认证请求
 *
 * @author zai 2020-04-07 10:57:11
 */
public class DataTransferReqHandler implements MessageDataHandler<DataTransferReq> {

	private SessionGroupServerConfig config;

	public DataTransferReqHandler(SessionGroupServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void handle(MessageData<DataTransferReq> request) {
		DataTransferReq req = request.getMessage();
		
		GGSession groupSession = request.getSession();
		String groupSessionId = groupSession.getSessonId();
		
		GGServer serviceServer = config.getServiceServer();
		GGServerConfig serviceServerConfig = serviceServer.getConfig();
		
		//判断是否开启业务服务端
		if (this.config.isEnableServiceServer()) {
			//获取传递的sessionid
			String tranferSessionId = req.getTranferSessionId();
			
			SessionManager sessionManager = serviceServerConfig.getSessionManager();
			//创建业务服务端session
			ServiceServerSession serviceSession = (ServiceServerSession) sessionManager.getSession(tranferSessionId);
			if (serviceSession == null) {
				String groupId = groupSession.getAttribute(SessionGroupServerSessionKeys.GROUP_SESSION_GROUP_ID, String.class);
				serviceSession = new ServiceServerSession(tranferSessionId, groupId, config.getSessionGroupManager(), serviceServerConfig);
				GGSession addSessionIfAbsent = sessionManager.addSessionIfAbsent(serviceSession);
				if (addSessionIfAbsent != null) {
					serviceSession = (ServiceServerSession) addSessionIfAbsent;
				}else {
					if (req.getTranferSessionId() == null) {
						request.getSession().addDisconnectListener(se -> {
							sessionManager.remove(groupSessionId);
						});
					}
				}
			}
			
			//提交任务到业务服务端
			Pack pack = new Pack(serviceSession, req.getAction(), req.getMessage());
			serviceServer.submitTask(new MessageDataTask(pack , serviceServerConfig));
		}
	}

}
