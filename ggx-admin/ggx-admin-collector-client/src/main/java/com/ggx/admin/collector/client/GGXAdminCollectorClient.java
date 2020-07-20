package com.ggx.admin.collector.client;

import com.ggx.admin.collector.client.collector.ServerDataCollector;
import com.ggx.admin.collector.client.collector.ServiceDataCollector;
import com.ggx.admin.collector.client.collector.task.CollectorTaskManager;
import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.collector.client.events.ConnCloseEventListener;
import com.ggx.admin.collector.client.events.ConnOpenEventListener;
import com.ggx.admin.collector.client.filter.AuthSendMessageFilter;
import com.ggx.admin.collector.client.handler.AuthRespHandler;
import com.ggx.admin.collector.client.handler.ServerDataRequestRespHandler;
import com.ggx.core.client.GGClient;
import com.ggx.core.client.config.GGClientConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.message.receive.support.ReceiveMessageSupport;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;

public class GGXAdminCollectorClient implements ReceiveMessageSupport, EventSupport{

	private GGXAdminCollectorClientConfig config;


	public GGXAdminCollectorClient(GGXAdminCollectorClientConfig config) {
		this.config = config;
		this.config.setCollectorClient(this);
		init();
	}
	
	private void init() {
		
		//CollectorTaskManager collectorTaskManager = this.config.getCollectorTaskManager();
		//collectorTaskManager.addCollector(new ServerDataCollector(config));
		//collectorTaskManager.addCollector(new ServiceDataCollector(config));
		
		
		GGClientConfig ggConfig = new GGClientConfig();
		ggConfig.setPingPongEnabled(true);
		ggConfig.setPrintPingPongInfo(config.isPrintPingPongInfo());
		ggConfig.setTaskExecutor(config.getTaskExecutor());
		ggConfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggConfig.setWorkerGroupThreadFactory(new GGThreadFactory("admin-collector-", false));
		ggConfig.init();

		GGClient ggClient = new GGClient(ggConfig);
		
		ggClient.onMessage(new AuthRespHandler(this.config));
		ggClient.onMessage(new ServerDataRequestRespHandler(this.config));
		
		ggClient.addEventListener(GGEvents.Connection.OPENED, new ConnOpenEventListener(config));
		ggClient.addEventListener(GGEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		
		ggClient.addFilter(new AuthSendMessageFilter(config));
		
		config.setServiceClient(ggClient);
	}
	
	

	public void start() {
		connect();
	}

	public void connect() {
		GGClient ggClient = config.getServiceClient();
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


	public GGXAdminCollectorClientConfig getConfig() {
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
