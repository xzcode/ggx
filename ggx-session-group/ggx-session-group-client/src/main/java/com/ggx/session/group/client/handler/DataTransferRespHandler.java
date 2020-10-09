package com.ggx.session.group.client.handler;

import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.message.receive.task.MessageDataTask;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.group.common.message.resp.DataTransferResp;
import com.ggx.session.group.client.config.SessionGroupClientConfig;



/**
 * 数据传输推送
 *
 * @author zai
 * 2020-04-09 15:03:50
 */
public class DataTransferRespHandler  {

	private SessionGroupClientConfig config;

	public DataTransferRespHandler(SessionGroupClientConfig config) {
		super();
		this.config = config;
	}

	@GGXAction
	public void handle(DataTransferResp resp) {
		
		
		String tranferSessionId = resp.getTranferSessionId();
		
		//判断是否开启业务客户端
		if (this.config.isEnableServiceClient()) {
			GGXCoreClient serviceClient = this.config.getServiceClient();
			GGXCoreClientConfig serviceClientConfig = serviceClient.getConfig();
			SessionManager sessionManager = serviceClient.getSessionManager();
			GGXSession session = sessionManager.getSession(tranferSessionId);
			if (session != null) {
				//提交任务到业务客户端
				Pack pack = new Pack(session, resp.getAction(), resp.getMessage());
				pack.setSerializeType(resp.getSerializeType());
				new MessageDataTask(pack , serviceClientConfig).run();;
			}
			
		}
		
		//判断是否开启业务服务端
		if (this.config.isEnableServiceServer() && this.config.getServiceServer() != null) {
			GGXCoreServer serviceServer = this.config.getServiceServer();
			GGXCoreServerConfig serviceServerConfig = serviceServer.getConfig();
			SessionManager sessionManager = serviceServerConfig.getSessionManager();
			GGXSession session = sessionManager.getSession(tranferSessionId);
			if (session != null) {
				//提交任务到业务客户端
				Pack pack = new Pack(session, resp.getAction(), resp.getMessage());
				if (resp.getSerializeType() != null) {
					resp.setSerializeType(pack.getSerializeType());
				}
				new MessageDataTask(pack , serviceServerConfig).run();;
			}
		}
		
	}

}
