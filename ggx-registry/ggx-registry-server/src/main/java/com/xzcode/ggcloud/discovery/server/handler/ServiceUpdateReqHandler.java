package com.xzcode.ggcloud.discovery.server.handler;

import com.xzcode.ggcloud.discovery.common.message.req.DiscoveryServiceUpdateReq;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryAddServiceResp;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceUpdateResp;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;
import com.xzcode.ggcloud.discovery.common.service.ServiceManager;
import com.xzcode.ggcloud.discovery.server.config.DiscoveryServerConfig;
import com.xzcode.ggserver.core.common.message.MessageData;
import com.xzcode.ggserver.core.common.message.request.action.MessageDataHandler;
import com.xzcode.ggserver.core.common.session.GGSession;

/**
 * 服务更新请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceUpdateReqHandler implements MessageDataHandler<DiscoveryServiceUpdateReq>{
	
	private DiscoveryServerConfig config;

	public ServiceUpdateReqHandler(DiscoveryServerConfig config) {
		this.config = config;
	}

	@Override
	public void handle(MessageData<DiscoveryServiceUpdateReq> request) {
		GGSession session = request.getSession();
		DiscoveryServiceUpdateReq req = request.getMessage();
		ServiceManager serviceManager = config.getServiceManager();
		
		ServiceInfo service = req.getServiceInfo();
		service.setSession(session);
		service.setHost(session.getHost());
		
		serviceManager.updateService(service);
		
		ServiceInfo updated = serviceManager.getService(service.getServiceId());
		DiscoveryServiceUpdateResp resp = new DiscoveryServiceUpdateResp(updated);
		
		//发送给所有服务客户端,服务信息更新
		serviceManager.sendToAllServices(resp);
	}


}
