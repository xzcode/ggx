package com.ggx.registry.client.handler;

import java.util.List;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.client.listener.IClientRegisterSuccessListener;
import com.ggx.registry.common.message.req.DiscoveryServiceListReq;
import com.ggx.registry.common.message.resp.DiscoveryServiceRegisterResp;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class RegisterRespHandler implements MessageDataHandler<DiscoveryServiceRegisterResp>{
	
	private RegistryClientConfig config;
	

	public RegisterRespHandler(RegistryClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<DiscoveryServiceRegisterResp> request) {
		DiscoveryServiceRegisterResp resp = request.getMessage();
		if (resp.isSuccess()) {
			config.getSession().send(DiscoveryServiceListReq.DEFAULT_INSTANT);
			RegistryClient discoveryClient = config.getDiscoveryClient();
			List<IClientRegisterSuccessListener> registerSuccessListeners = discoveryClient.getRegisterSuccessListeners();
			for (IClientRegisterSuccessListener listener : registerSuccessListeners) {
				listener.onRegisterSuccess();
			}
		}
	}

	

}
