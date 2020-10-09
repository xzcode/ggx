package com.ggx.registry.client.handler;

import java.util.List;

import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.client.listener.ClientRegisterSuccessListener;
import com.ggx.registry.common.message.req.RegistryServiceListReq;
import com.ggx.registry.common.message.resp.RegistryServiceRegisterResp;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class RegisterRespHandler{
	
	private RegistryClientConfig config;
	
	public RegisterRespHandler(RegistryClientConfig config) {
		super();
		this.config = config;
	}



	@GGXAction
	public void handle(RegistryServiceRegisterResp resp) {
		if (resp.isSuccess()) {
			
			RegistryClient registryClient = config.getRegistryClient();
			
			config.getCustomData().putAll(resp.getServiceInfo().getCustomData());
			
			List<ClientRegisterSuccessListener> registerSuccessListeners = registryClient.getRegisterSuccessListeners();
			for (ClientRegisterSuccessListener listener : registerSuccessListeners) {
				try {
					listener.onRegisterSuccess();
				} catch (Exception e) {
					GGXLogUtil.getLogger(this).error("ClientRegisterSuccessListener ERROR!", e);
				}
			}
			if (config.isRequireServices()) {
				config.getSession().send(RegistryServiceListReq.ALL_SERVICE_INSTANT);
			}
		}
	}

	

}
