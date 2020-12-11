package com.ggx.session.group.client.handler;

import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
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
	public void handle(DataTransferResp resp, GGXSession session) {
		
		
		GGXCoreClient serviceClient = this.config.getServiceClient();
		GGXCoreClientConfig serviceClientConfig = serviceClient.getConfig();
		SessionManager seviceClientSessionManager = serviceClient.getSessionManager();
		GGXSession seviceClientSession = seviceClientSessionManager.getSession(session.getSessionId());
		if (seviceClientSession != null) {
			//提交任务到业务客户端
			Pack pack = new Pack(seviceClientSession, resp.getAction(), resp.getMessage());
			serviceClientConfig.getReceiveMessageManager().receive(pack);
		}
	}

}
