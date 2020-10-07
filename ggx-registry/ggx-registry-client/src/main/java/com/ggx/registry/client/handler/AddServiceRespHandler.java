package com.ggx.registry.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.handler.MessageHandler;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.common.message.resp.RegistryAddServiceResp;

/**
 * 新增服务推送处理器
 * 
 * @author zai
 * 2020-02-10 19:57:04
 */
public class AddServiceRespHandler implements MessageHandler<RegistryAddServiceResp>{
	
	private RegistryClientConfig config;
	

	public AddServiceRespHandler(RegistryClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void handle(MessageData<RegistryAddServiceResp> request) {
		
		RegistryAddServiceResp resp = request.getMessage();
		config.getServiceManager().registerService(resp.getServiceInfo());
		
	}

	

}
