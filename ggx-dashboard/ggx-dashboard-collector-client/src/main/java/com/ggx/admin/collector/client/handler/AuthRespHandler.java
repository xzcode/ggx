package com.ggx.admin.collector.client.handler;

import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.common.collector.message.resp.AuthResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.utils.logger.GGLoggerUtil;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class AuthRespHandler implements MessageHandler<AuthResp>{
	
	private GGXAdminCollectorClientConfig config;
	

	public AuthRespHandler(GGXAdminCollectorClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<AuthResp> request) {
		AuthResp resp = request.getMessage();
		
		if (!resp.isSuccess()) {
			GGLoggerUtil.getLogger(this).error("Auth Failed!!");
			config.getServiceClient().shutdown();
			return;
		}
		
		this.config.setAuthed(true);
		
	}

	

}
