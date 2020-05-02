package com.ggx.registry.client.handler;

import java.util.List;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.client.listener.IClientRegisterSuccessListener;
import com.ggx.registry.common.message.req.RegistryServiceListReq;
import com.ggx.registry.common.message.resp.RegistryServiceRegisterResp;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class RegisterRespHandler implements MessageDataHandler<RegistryServiceRegisterResp>{
	
	private RegistryClientConfig config;
	

	public RegisterRespHandler(RegistryClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public void handle(MessageData<RegistryServiceRegisterResp> messageData) {
		RegistryServiceRegisterResp resp = messageData.getMessage();
		if (resp.isSuccess()) {
			config.getSession().send(RegistryServiceListReq.DEFAULT_INSTANT);
			RegistryClient discoveryClient = config.getDiscoveryClient();
			List<IClientRegisterSuccessListener> registerSuccessListeners = discoveryClient.getRegisterSuccessListeners();
			for (IClientRegisterSuccessListener listener : registerSuccessListeners) {
				listener.onRegisterSuccess();
			}
		}
	}

	

}
