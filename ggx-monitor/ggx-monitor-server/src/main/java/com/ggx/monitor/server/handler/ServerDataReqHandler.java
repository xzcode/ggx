package com.ggx.monitor.server.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.monitor.common.data.model.server.ServerData;
import com.ggx.monitor.common.data.model.service.ServiceData;
import com.ggx.monitor.common.message.req.AuthReq;
import com.ggx.monitor.common.message.req.ServiceDataReq;
import com.ggx.monitor.common.message.resp.AuthResp;
import com.ggx.monitor.server.config.GameMonitorServerConfig;
import com.ggx.monitor.server.constant.GameMonitorServerSessionKeys;

/**
 * 服务器数据请求处理器
 *
 * @author zai
 * 2020-06-24 18:15:42
 */
public class ServerDataReqHandler implements MessageHandler<ServerData>{
	
	private GameMonitorServerConfig config;
	

	public ServerDataReqHandler(GameMonitorServerConfig config) {
		this.config = config;
	}
	


	@Override
	public void handle(MessageData<ServerData> messageData) {
		GGSession session = messageData.getSession();
	}


	

}
