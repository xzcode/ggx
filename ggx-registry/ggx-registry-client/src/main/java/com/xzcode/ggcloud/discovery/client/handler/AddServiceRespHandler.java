package com.xzcode.ggcloud.discovery.client.handler;

import com.xzcode.ggcloud.discovery.client.config.DiscoveryClientConfig;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryAddServiceResp;
import com.xzcode.ggserver.core.common.message.MessageData;
import com.xzcode.ggserver.core.common.message.request.action.MessageDataHandler;

/**
 * 新增服务推送处理器
 * 
 * @author zai
 * 2020-02-10 19:57:04
 */
public class AddServiceRespHandler implements MessageDataHandler<DiscoveryAddServiceResp>{
	
	private DiscoveryClientConfig config;
	

	public AddServiceRespHandler(DiscoveryClientConfig config) {
		super();
		this.config = config;
	}


	@Override
	public void handle(MessageData<DiscoveryAddServiceResp> request) {
		
		DiscoveryAddServiceResp resp = request.getMessage();
		config.getServiceManager().registerService(resp.getServiceInfo());
		
	}

	

}
