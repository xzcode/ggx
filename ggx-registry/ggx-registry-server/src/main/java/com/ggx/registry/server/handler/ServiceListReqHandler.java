package com.ggx.registry.server.handler;

import java.util.List;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.common.message.req.DiscoveryServiceListReq;
import com.ggx.registry.common.message.resp.DiscoveryServiceListResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.server.config.RegistryServerConfig;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceListReqHandler implements MessageDataHandler<DiscoveryServiceListReq>{
	
	private RegistryServerConfig config;

	public ServiceListReqHandler(RegistryServerConfig config) {
		this.config = config;
	}

	@Override
	public void handle(MessageData<DiscoveryServiceListReq> request) {
		GGSession session = request.getSession();
		List<ServiceInfo> serviceList = config.getServiceManager().getServiceList();
		DiscoveryServiceListResp resp = new DiscoveryServiceListResp(serviceList);
		session.send(resp);
	}


}
