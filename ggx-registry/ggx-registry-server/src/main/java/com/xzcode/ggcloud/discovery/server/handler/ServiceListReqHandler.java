package com.xzcode.ggcloud.discovery.server.handler;

import java.util.List;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.xzcode.ggcloud.discovery.common.message.req.DiscoveryServiceListReq;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceListResp;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;
import com.xzcode.ggcloud.discovery.server.config.DiscoveryServerConfig;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai
 * 2019-10-04 14:29:53
 */
public class ServiceListReqHandler implements MessageDataHandler<DiscoveryServiceListReq>{
	
	private DiscoveryServerConfig config;

	public ServiceListReqHandler(DiscoveryServerConfig config) {
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
