package com.ggx.registry.server.handler;

import java.util.List;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.common.message.req.DiscoveryServiceRegisterReq;
import com.ggx.registry.common.message.req.model.ServiceInfoModel;
import com.ggx.registry.common.message.resp.DiscoveryAddServiceResp;
import com.ggx.registry.common.message.resp.DiscoveryServiceRegisterResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.registry.server.constant.DiscoveryServerSessionKeys;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class RegisterReqHandler implements MessageDataHandler<DiscoveryServiceRegisterReq>{
	
	private RegistryServerConfig config;
	

	public RegisterReqHandler(RegistryServerConfig config) {
		super();
		this.config = config;
	}
	


	@Override
	public void handle(MessageData<DiscoveryServiceRegisterReq> request) {
		GGSession session = request.getSession();
		DiscoveryServiceRegisterReq req = request.getMessage();
		String serverAuthToken = config.getAuthToken();
		//判断认证token是否正确
		if (serverAuthToken != null && !serverAuthToken.isEmpty()) {
			String clientAuthToken = req.getAuthToken();
			if (clientAuthToken == null || clientAuthToken.isEmpty() || !clientAuthToken.equals(serverAuthToken)) {
				session.send(new DiscoveryServiceRegisterResp(false, "Auth Token Is Incorrect"));
				return;
			}
			
		}
		ServiceInfo infoModel = req.getServiceInfo();
		ServiceInfo serviceInfo = session.getAttribute(DiscoveryServerSessionKeys.SERVICE_INFO, ServiceInfo.class);
		
		ServiceManager serviceManager = config.getServiceManager();
		
		if (serviceInfo == null) {
			serviceInfo = infoModel;
			serviceInfo.setHost(session.getHost());
			serviceInfo.setTimeoutDelay(config.getServiceTimeoutDelay());
			serviceInfo.setSession(session);
			serviceManager.registerService(serviceInfo);
			session.addAttribute(DiscoveryServerSessionKeys.SERVICE_INFO, serviceInfo);
		}
		session.send(new DiscoveryServiceRegisterResp(true));
		
		//发送给所有服务客户端,新服务已上线
		serviceManager.sendToAllServices(new DiscoveryAddServiceResp(serviceInfo));
	}


	

}
