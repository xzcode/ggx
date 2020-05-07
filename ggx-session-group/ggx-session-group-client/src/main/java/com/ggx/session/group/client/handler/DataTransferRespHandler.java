package com.ggx.session.group.client.handler;

import com.ggx.core.client.GGClient;
import com.ggx.core.client.config.GGClientConfig;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.message.request.task.MessageDataTask;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.group.common.message.resp.DataTransferResp;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;



/**
 * 数据传输推送
 *
 * @author zai
 * 2020-04-09 15:03:50
 */
public class DataTransferRespHandler implements MessageDataHandler<DataTransferResp> {

	private SessionGroupClientConfig config;

	public DataTransferRespHandler(SessionGroupClientConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void handle(MessageData<DataTransferResp> messageData) {
		
		DataTransferResp resp = messageData.getMessage();
		
		String tranferSessionId = resp.getTranferSessionId();
		
		//判断是否开启业务客户端
		if (this.config.isEnableServiceClient()) {
			GGClient serviceClient = this.config.getServiceClient();
			GGClientConfig serviceClientConfig = serviceClient.getConfig();
			SessionManager sessionManager = serviceClient.getSessionManager();
			GGSession session = sessionManager.getSession(tranferSessionId);
			if (session != null) {
				//提交任务到业务客户端
				Pack pack = new Pack(session, resp.getAction(), resp.getMessage());
				serviceClient.submitTask(new MessageDataTask(pack , serviceClientConfig));
			}
			
		}
		
		//判断是否开启业务服务端
		if (this.config.isEnableServiceServer() && this.config.getServiceServer() != null) {
			GGServer serviceServer = this.config.getServiceServer();
			GGServerConfig serviceServerConfig = serviceServer.getConfig();
			SessionManager sessionManager = serviceServerConfig.getSessionManager();
			GGSession session = sessionManager.getSession(tranferSessionId);
			if (session != null) {
				//提交任务到业务客户端
				Pack pack = new Pack(session, resp.getAction(), resp.getMessage());
				serviceServer.submitTask(new MessageDataTask(pack , serviceServerConfig));
			}
		}
		
	}

}
