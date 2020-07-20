package com.ggx.admin.collector.client.handler;

import com.ggx.admin.collector.client.collector.ServerDataCollector;
import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.common.collector.message.req.ServerDataReq;
import com.ggx.admin.common.collector.message.resp.ServerDataRequestResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai 2019-10-04 14:29:53
 */
public class ServerDataRequestRespHandler implements MessageHandler<ServerDataRequestResp> {

	private GGXAdminCollectorClientConfig config;

	private ServerDataCollector serverDataCollector;

	public ServerDataRequestRespHandler(GGXAdminCollectorClientConfig config) {
		super();
		this.config = config;
		serverDataCollector = new ServerDataCollector(config);
	}

	@Override
	public void handle(MessageData<ServerDataRequestResp> message) {
		GGSession session = message.getSession();
		session.send(new ServerDataReq(serverDataCollector.collect()));
	}

}
