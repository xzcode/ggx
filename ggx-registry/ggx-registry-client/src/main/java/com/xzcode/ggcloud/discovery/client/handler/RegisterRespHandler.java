package com.xzcode.ggcloud.discovery.client.handler;

import java.util.List;

import com.xzcode.ggcloud.discovery.client.DiscoveryClient;
import com.xzcode.ggcloud.discovery.client.config.DiscoveryClientConfig;
import com.xzcode.ggcloud.discovery.client.listener.IClientRegisterSuccessListener;
import com.xzcode.ggcloud.discovery.common.message.req.DiscoveryServiceListReq;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceRegisterResp;
import com.xzcode.ggserver.core.common.message.MessageData;
import com.xzcode.ggserver.core.common.message.request.action.MessageDataHandler;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class RegisterRespHandler implements MessageDataHandler<DiscoveryServiceRegisterResp>{
	
	private DiscoveryClientConfig config;
	

	public RegisterRespHandler(DiscoveryClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<DiscoveryServiceRegisterResp> request) {
		DiscoveryServiceRegisterResp resp = request.getMessage();
		if (resp.isSuccess()) {
			config.getSession().send(DiscoveryServiceListReq.DEFAULT_INSTANT);
			DiscoveryClient discoveryClient = config.getDiscoveryClient();
			List<IClientRegisterSuccessListener> registerSuccessListeners = discoveryClient.getRegisterSuccessListeners();
			for (IClientRegisterSuccessListener listener : registerSuccessListeners) {
				listener.onRegisterSuccess();
			}
		}
	}

	

}
