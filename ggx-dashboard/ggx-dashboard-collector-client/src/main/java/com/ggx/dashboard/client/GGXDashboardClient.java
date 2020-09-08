package com.ggx.dashboard.client;

import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.message.receive.support.ReceiveMessageSupport;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.dashboard.client.config.GGXDashboardClientConfig;
import com.ggx.dashboard.client.events.ConnCloseEventListener;
import com.ggx.dashboard.client.events.ConnOpenEventListener;
import com.ggx.dashboard.client.filter.AuthSendMessageFilter;
import com.ggx.dashboard.client.handler.AuthRespHandler;
import com.ggx.dashboard.client.handler.CollectServiceDataHandler;

public class GGXDashboardClient implements ReceiveMessageSupport, EventSupport{

	private GGXDashboardClientConfig config;


	public GGXDashboardClient(GGXDashboardClientConfig config) {
		this.config = config;
		this.config.setCollectorClient(this);
		init();
	}
	
	private void init() {
		
		//CollectorTaskManager collectorTaskManager = this.config.getCollectorTaskManager();
		//collectorTaskManager.addCollector(new ServerDataCollector(config));
		//collectorTaskManager.addCollector(new ServiceDataCollector(config));
		
		
		GGXCoreClientConfig ggConfig = new GGXCoreClientConfig();
		ggConfig.setPingPongEnabled(true);
		ggConfig.setPrintPingPongInfo(config.isPrintPingPongInfo());
		ggConfig.setTaskExecutor(config.getTaskExecutor());
		ggConfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggConfig.setWorkerGroupThreadFactory(new GGThreadFactory("admin-collector-", false));
		ggConfig.init();

		GGXCoreClient ggClient = new GGXCoreClient(ggConfig);
		
		ggClient.onMessage(new AuthRespHandler(this.config));
		ggClient.onMessage(new CollectServiceDataHandler(this.config));
		
		ggClient.addEventListener(GGXCoreEvents.Connection.OPENED, new ConnOpenEventListener(config));
		ggClient.addEventListener(GGXCoreEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		
		ggClient.addFilter(new AuthSendMessageFilter(config));
		
		config.setServiceClient(ggClient);
	}
	
	

	public void start() {
		connect();
	}

	public void connect() {
		GGXCoreClient ggClient = config.getServiceClient();
		String host = config.getServerHost();
		int port = config.getServerPort();
		ggClient.connect(host, port).addListener(f -> {
			if (!f.isSuccess()) {
				// 连接失败，进行进行重连操作
				GGLoggerUtil.getLogger(this).info("Game Data Collector Client Connect Server[{}:{}] Failed!", host, port);
				ggClient.schedule(config.getTryRegisterInterval(), () -> {
					connect();
				});
				return;
			}
			GGLoggerUtil.getLogger(this).info("Game Data Collector Client Connect Server[{}:{}] Successfully!", host, port);
		});
	}


	public GGXDashboardClientConfig getConfig() {
		return config;
	}

	@Override
	public EventManager getEventManager() {
		return this.getConfig().getServiceClient().getEventManager();
	}

	@Override
	public ReceiveMessageManager getReceiveMessageManager() {
		return this.getConfig().getServiceClient().getReceiveMessageManager();
	}

}
