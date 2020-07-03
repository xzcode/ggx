package com.ggx.admin.collector.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.admin.common.collector.data.model.server.ServerData;
import com.ggx.admin.common.collector.message.req.ServerDataReq;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.spring.support.annotation.GGXMessageHandler;

/**
 * 服务器数据请求处理器
 *
 * @author zai
 * 2020-06-24 18:15:42
 */
public class ServerDataReqHandler implements MessageHandler<ServerDataReq>{
	
	private GGXAdminCollectorServerConfig config;
	
	

	public ServerDataReqHandler(GGXAdminCollectorServerConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void handle(MessageData<ServerDataReq> messageData) {
		GGSession session = messageData.getSession();
		ServerData serverData = messageData.getMessage().getServerData();
		System.out.println(serverData);
		
	}


	

}
