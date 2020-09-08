package com.ggx.dashboard.client.handler;

import com.ggx.admin.common.collector.message.req.ServiceDataReq;
import com.ggx.admin.common.collector.message.resp.CollectServiceDataResp;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.dashboard.client.collector.ServiceDataCollector;
import com.ggx.dashboard.client.config.GGXDashboardClientConfig;

/**
 * 客户端注册请求处理
 * 
 * 
 * @author zai 2019-10-04 14:29:53
 */
public class CollectServiceDataHandler implements MessageHandler<CollectServiceDataResp> {

	private GGXDashboardClientConfig config;

	private ServiceDataCollector serviceDataCollector;
	

	public CollectServiceDataHandler(GGXDashboardClientConfig config) {
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
