package com.ggx.group.server.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.message.receive.task.MessageDataTask;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.constant.GGDefaultSessionKeys;
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
public class DataTransferReqHandler implements MessageHandler<DataTransferReq> {

	private SessionGroupServerConfig config;

	public DataTransferReqHandler(SessionGroupServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void handle(MessageData<DataTransferReq> messageData) {
		DataTransferReq req = messageData.getMessage();
		
		GGSession groupSession = messageData.getSession();
		String groupSessionId = groupSession.getSessonId();
		
		GGXCoreServer serviceServer = config.getServiceServer();
		GGXCoreServerConfig serviceServerConfig = serviceServer.getConfig();
		
		//判断是否开启自定义传输数据处理器
		if (this.config.isEnableCustomDataTransferHandler() && this.config.getCustomDataTransferHandler() != null) {
			CustomDataTransferHandler customDataTransferHandler = this.config.getCustomDataTransferHandler();
			customDataTransferHandler.handle(messageData);
			return;//开启自定义处理后，不再进行后续处理
		}
		
		//判断是否开启业务服务端
		if (this.config.isEnableServiceServer()) {
			//获取传递的sessionid
			String tranferSessionId = req.getTranferSessionId();
			
			SessionManager sessionManager = serviceServerConfig.getSessionManager();
			//创建业务服务端session
			GroupServiceServerSession serviceSession = (GroupServiceServerSession) sessionManager.getSession(tranferSessionId);
			if (serviceSession == null) {
				String groupId = groupSession.getAttribute(SessionGroupServerSessionKeys.GROUP_SESSION_GROUP_ID, String.class);
				serviceSession = new GroupServiceServerSession(tranferSessionId, groupId, config.getSessionGroupManager(), serviceServerConfig);
				if (req.getSerializeType() != null) {
					serviceSession.addAttribute(GGDefaultSessionKeys.SERIALIZE_TYPE, req.getSerializeType());
				}
				GGSession addSessionIfAbsent = sessionManager.addSessionIfAbsent(serviceSession);
				if (addSessionIfAbsent != null) {
					serviceSession = (GroupServiceServerSession) addSessionIfAbsent;
				}else {
					if (req.getTranferSessionId() == null) {
						messageData.getSession().addDisconnectListener(se -> {
							sessionManager.remove(groupSessionId);
						});
					}
				}
			}
			
			//提交任务到业务服务端
			Pack pack = new Pack(serviceSession, req.getAction(), req.getMessage());
			pack.setSerializeType(req.getSerializeType());
			new MessageDataTask(pack , serviceServerConfig).run();
		}
	}

}
