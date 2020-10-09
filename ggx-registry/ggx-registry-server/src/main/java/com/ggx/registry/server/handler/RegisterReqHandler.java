package com.ggx.registry.server.handler;

import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.registry.common.message.req.RegistryServiceRegisterReq;
import com.ggx.registry.common.message.resp.RegistryAddServiceResp;
import com.ggx.registry.common.message.resp.RegistryServiceRegisterResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.registry.server.constant.RegistryServerSessionKeys;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class RegisterReqHandler{
	
	private RegistryServerConfig config;
	

	public RegisterReqHandler(RegistryServerConfig config) {
		super();
		this.config = config;
	}
	


	@GGXAction
	public void handle(RegistryServiceRegisterReq req, GGXSession session) {
		String serverAuthToken = config.getAuthToken();
		//判断认证token是否正确
		if (serverAuthToken != null && !serverAuthToken.isEmpty()) {
			String clientAuthToken = req.getAuthToken();
			if (clientAuthToken == null || clientAuthToken.isEmpty() || !clientAuthToken.equals(serverAuthToken)) {
				session.send(new RegistryServiceRegisterResp(null,false, "Auth Token Is Incorrect"));
				return;
			}
			
		}
		ServiceInfo infoModel = req.getServiceInfo();
		//String serviceId = infoModel.getServiceId();
		//ServiceInfo serviceInfo = session.getAttribute(RegistryServerSessionKeys.SERVICE_INFO, ServiceInfo.class);
		
		ServiceManager serviceManager = config.getServiceManager();
		
		infoModel.setHost(session.getHost());
		infoModel.setSession(session);
		session.addAttribute(RegistryServerSessionKeys.SERVICE_INFO, infoModel);
		ServiceInfo oldServiceInfo = serviceManager.registerService(infoModel);
		
		GGXLogUtil.getLogger(this).warn("Register service! serviceName: {}, serviceId: {}", infoModel.getServiceName(), infoModel.getServiceId());
		
		if (oldServiceInfo != null) {
			oldServiceInfo.getSession().disconnect();
			GGXLogUtil.getLogger(this).warn("Disconnecting old service! serviceName: {}, serviceId: {}", oldServiceInfo.getServiceName(), oldServiceInfo.getServiceId());
		}
		
		session.send(new RegistryServiceRegisterResp(infoModel, true));
		
		//发送给所有服务客户端,新服务已上线
		serviceManager.sendToAllServices(new RegistryAddServiceResp(infoModel));
	}


	

}
