package com.ggx.registry.server.handler;

import java.util.List;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGXSession;
import com.ggx.registry.common.message.req.RegistryServiceListReq;
import com.ggx.registry.common.message.resp.RegistryServiceListResp;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.server.config.RegistryServerConfig;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceListReqHandler implements MessageHandler<RegistryServiceListReq>{
	
	private RegistryServerConfig config;

	public ServiceListReqHandler(RegistryServerConfig config) {
		this.config = config;
	}

	@Override
	public void handle(MessageData<RegistryServiceListReq> request) {
		GGXSession session = request.getSession();
		List<ServiceInfo> serviceList = config.getServiceManager().getServiceList();
		RegistryServiceListResp resp = new RegistryServiceListResp(serviceList);
		session.send(resp);
	}


}
