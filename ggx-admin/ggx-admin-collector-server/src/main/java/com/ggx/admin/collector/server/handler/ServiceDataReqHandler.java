package com.ggx.admin.collector.server.handler;

import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.admin.common.collector.message.resp.CollectServiceDataResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;

/**
 * 服务数据请求处理器
 *
 * @author zai
 * 2020-06-24 18:15:42
 */
public class ServiceDataReqHandler implements MessageHandler<CollectServiceDataResp>{
	
	private GGXAdminCollectorServerConfig config;
	

	public ServiceDataReqHandler(GGXAdminCollectorServerConfig config) {
		this.config = config;
	}
	


	@Override
	public void handle(MessageData<CollectServiceDataResp> messageData) {
	}


	

}
