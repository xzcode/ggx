package com.ggx.admin.collector.client.handler;

import com.ggx.admin.collector.client.collector.ServiceDataCollector;
import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.common.collector.message.req.ServiceDataReq;
import com.ggx.admin.common.collector.message.resp.CollectServiceDataResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai 2019-10-04 14:29:53
 */
public class CollectServiceDataHandler implements MessageHandler<CollectServiceDataResp> {

	private GGXAdminCollectorClientConfig config;

	private ServiceDataCollector serviceDataCollector;
	

	public CollectServiceDataHandler(GGXAdminCollectorClientConfig config) {
		super();
		this.config = config;
		serviceDataCollector = new ServiceDataCollector(config);
	}

	@Override
	public void handle(MessageData<CollectServiceDataResp> message) {
		GGSession session = message.getSession();
		session.send(new ServiceDataReq(serviceDataCollector.collect()));
	}

}
