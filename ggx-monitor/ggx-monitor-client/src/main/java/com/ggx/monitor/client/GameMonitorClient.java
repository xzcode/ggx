package com.ggx.monitor.client;

import java.util.ArrayList;
import java.util.List;

import com.ggx.core.client.GGClient;
import com.ggx.core.client.config.GGClientConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.message.receive.support.ReceiveMessageSupport;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.monitor.client.config.GameMonitorClientConfig;
import com.ggx.monitor.client.events.ConnCloseEventListener;
import com.ggx.monitor.client.events.ConnOpenEventListener;
import com.ggx.monitor.client.handler.AuthRespHandler;
import com.ggx.monitor.client.listener.IClientRegisterSuccessListener;
import com.ggx.monitor.common.message.resp.AuthResp;

public class GameMonitorClient implements ReceiveMessageSupport, EventSupport{

	private GameMonitorClientConfig config;

	protected List<IClientRegisterSuccessListener> registerSuccessListeners = new ArrayList<>();

	public GameMonitorClient(GameMonitorClientConfig config) {
		this.config = config;
		this.config.setGameMonitorClient(this);
		init();
	}
	
	private void init() {
		GGClientConfig ggConfig = new GGClientConfig();
		ggConfig.setPingPongEnabled(true);
		ggConfig.setPrintPingPongInfo(config.isPrintPingPongInfo());
		ggConfig.setTaskExecutor(config.getTaskExecutor());
		ggConfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggConfig.setWorkerGroupThreadFactory(new GGThreadFactory("monitor-worker-", false));
		ggConfig.init();

		GGClient ggClient = new GGClient(ggConfig);
		config.setServiceClient(ggClient);

		ggClient.onMessage(AuthResp.ACTION, new AuthRespHandler(config));

		ggClient.addEventListener(GGEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		ggClient.addEventListener(GGEvents.Connection.OPENED, new ConnOpenEventListener(config));
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
				GGLoggerUtil.getLogger(this).info("Game Monitor Client Connect Server[{}:{}] Failed!", host, port);
				ggClient.schedule(config.getTryRegisterInterval(), () -> {
					connect();
				});
				return;
			}
			GGLoggerUtil.getLogger(this).info("Game Monitor Client Connect Server[{}:{}] Successfully!", host, port);
		});
	}


	public void addRegisterSuccessListener(IClientRegisterSuccessListener listener) {
		this.registerSuccessListeners.add(listener);
	}

	public List<IClientRegisterSuccessListener> getRegisterSuccessListeners() {
		return registerSuccessListeners;
	}

	public GameMonitorClientConfig getConfig() {
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
