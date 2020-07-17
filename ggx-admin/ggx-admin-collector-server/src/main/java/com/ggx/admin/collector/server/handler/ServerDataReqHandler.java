package com.ggx.admin.collector.server.handler;

import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.admin.collector.server.service.ServerDataService;
import com.ggx.admin.common.collector.data.model.server.ServerData;
import com.ggx.admin.common.collector.message.req.ServerDataReq;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;

/**
 * 服务器数据请求处理器
 *
 * @author zai 2020-06-24 18:15:42
 */
public class ServerDataReqHandler implements MessageHandler<ServerDataReq> {

	private ServerDataService serverDataService;
	private GGXAdminCollectorServerConfig config;

	public ServerDataReqHandler(ServerDataService serverDataService, GGXAdminCollectorServerConfig config) {
		this.config = config;
		this.serverDataService = config.getServerDataService();
	}

	@Override
	public void handle(MessageData<ServerDataReq> messageData) {
		ServerData serverData = messageData.getMessage().getServerData();
		serverDataService.addData(serverData.getServiceId(), serverData);

	}

}
