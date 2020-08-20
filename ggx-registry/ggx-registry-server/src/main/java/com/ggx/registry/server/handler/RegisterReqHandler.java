package com.ggx.registry.server.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.common.message.req.RegistryServiceRegisterReq;
import com.ggx.registry.common.message.resp.RegistryAddServiceResp;
import com.ggx.registry.common.message.resp.RegistryServiceRegisterResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.registry.server.constant.RegistryServerSessionKeys;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class RegisterReqHandler implements MessageHandler<RegistryServiceRegisterReq>{
	
	private RegistryServerConfig config;
	

	public RegisterReqHandler(RegistryServerConfig config) {
		super();
		this.config = config;
	}
	


	@Override
	public void handle(MessageData<RegistryServiceRegisterReq> request) {
		GGSession session = request.getSession();
		RegistryServiceRegisterReq req = request.getMessage();
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
		String serviceId = infoModel.getServiceId();
		//ServiceInfo serviceInfo = session.getAttribute(RegistryServerSessionKeys.SERVICE_INFO, ServiceInfo.class);
		
		ServiceManager serviceManager = config.getServiceManager();
		ServiceInfo oldServiceInfo = serviceManager.getService(serviceId);
		
		infoModel.setHost(session.getHost());
		infoModel.setSession(session);
		session.addAttribute(RegistryServerSessionKeys.SERVICE_INFO, infoModel);
		oldServiceInfo = serviceManager.registerService(infoModel);
		
		if (oldServiceInfo != null) {
			oldServiceInfo.getSession().disconnect();
		}
		
		session.send(new RegistryServiceRegisterResp(infoModel, true));
		
		//发送给所有服务客户端,新服务已上线
		serviceManager.sendToAllServices(new RegistryAddServiceResp(infoModel));
	}


	

}
