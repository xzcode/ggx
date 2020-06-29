package com.ggx.monitor.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.monitor.client.config.GameMonitorClientConfig;
import com.ggx.monitor.common.message.resp.AuthResp;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class AuthRespHandler implements MessageHandler<AuthResp>{
	
	private GameMonitorClientConfig config;
	

	public AuthRespHandler(GameMonitorClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<AuthResp> request) {
		AuthResp resp = request.getMessage();
		
		if (!resp.isSuccess()) {
			GGLoggerUtil.getLogger(this).error("Auth Failed!!");
			config.getServiceClient().shutdown();
		}
		
	}

	

}
