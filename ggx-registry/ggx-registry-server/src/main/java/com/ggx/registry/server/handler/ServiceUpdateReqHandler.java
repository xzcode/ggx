package com.ggx.registry.server.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.common.message.req.RegistryServiceUpdateReq;
import com.ggx.registry.common.message.resp.RegistryServiceUpdateResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.registry.server.config.RegistryServerConfig;

/**
 * 服务更新请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceUpdateReqHandler implements MessageDataHandler<RegistryServiceUpdateReq>{
	
	private RegistryServerConfig config;

	public ServiceUpdateReqHandler(RegistryServerConfig config) {
		this.config = config;
	}

	@Override
	public void handle(MessageData<RegistryServiceUpdateReq> request) {
		GGSession session = request.getSession();
		RegistryServiceUpdateReq req = request.getMessage();
		ServiceManager serviceManager = config.getServiceManager();
		
		ServiceInfo service = req.getServiceInfo();
		service.setSession(session);
		service.setHost(session.getHost());
		
		serviceManager.updateService(service);
		
		ServiceInfo updated = serviceManager.getService(service.getServiceId());
		RegistryServiceUpdateResp resp = new RegistryServiceUpdateResp(updated);
		
		//发送给所有服务客户端,服务信息更新
		serviceManager.sendToAllServices(resp);
	}


}
