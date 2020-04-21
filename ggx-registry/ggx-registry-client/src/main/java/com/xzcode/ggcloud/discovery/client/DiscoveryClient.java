package com.xzcode.ggcloud.discovery.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.xzcode.ggcloud.discovery.client.config.DiscoveryClientConfig;
import com.xzcode.ggcloud.discovery.client.events.ConnCloseEventListener;
import com.xzcode.ggcloud.discovery.client.events.ConnOpenEventListener;
import com.xzcode.ggcloud.discovery.client.handler.AddServiceRespHandler;
import com.xzcode.ggcloud.discovery.client.handler.RegisterRespHandler;
import com.xzcode.ggcloud.discovery.client.handler.ServiceListRespHandler;
import com.xzcode.ggcloud.discovery.client.handler.ServiceUnregisterRespHandler;
import com.xzcode.ggcloud.discovery.client.handler.ServiceUpdateRespHandler;
import com.xzcode.ggcloud.discovery.client.listener.IClientRegisterSuccessListener;
import com.xzcode.ggcloud.discovery.client.registry.RegistryInfo;
import com.xzcode.ggcloud.discovery.common.message.req.DiscoveryServiceListReq;
import com.xzcode.ggcloud.discovery.common.message.req.DiscoveryServiceUpdateReq;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryAddServiceResp;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceListResp;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceRegisterResp;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceUnregisterResp;
import com.xzcode.ggcloud.discovery.common.message.resp.DiscoveryServiceUpdateResp;
import com.xzcode.ggcloud.discovery.common.service.ServiceInfo;
import com.xzcode.ggserver.core.client.GGClient;
import com.xzcode.ggserver.core.client.config.GGClientConfig;
import com.xzcode.ggserver.core.common.constant.ProtocolTypeConstants;
import com.xzcode.ggserver.core.common.event.GGEvents;
import com.xzcode.ggserver.core.common.session.GGSession;
import com.xzcode.ggserver.core.common.utils.logger.GGLoggerUtil;

public class DiscoveryClient {
	
	private DiscoveryClientConfig config;
	
	protected List<IClientRegisterSuccessListener> registerSuccessListeners = new ArrayList<>();
	
	public DiscoveryClient(DiscoveryClientConfig config) {
		this.config = config;
		this.config.setDiscoveryClient(this);
	}

	public void start() {
		GGClientConfig ggConfig = new GGClientConfig();
		ggConfig.setPingPongEnabled(true);
		ggConfig.setPrintPingPongInfo(config.isPrintPingPongInfo());
		ggConfig.setTaskExecutor(config.getTaskExecutor());
		ggConfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggConfig.init();
		
		GGClient ggClient = new GGClient(ggConfig);
		config.setGGclient(ggClient);
		
		ggClient.onMessage(DiscoveryServiceRegisterResp.ACTION, new RegisterRespHandler(config));
		ggClient.onMessage(DiscoveryServiceListResp.ACTION, new ServiceListRespHandler(config));
		ggClient.onMessage(DiscoveryServiceUpdateResp.ACTION, new ServiceUpdateRespHandler(config));
		ggClient.onMessage(DiscoveryServiceUnregisterResp.ACTION, new ServiceUnregisterRespHandler(config));
		ggClient.onMessage(DiscoveryAddServiceResp.ACTION, new AddServiceRespHandler(config));
		
		ggClient.addEventListener(GGEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		ggClient.addEventListener(GGEvents.Connection.OPENED, new ConnOpenEventListener(config));
		
		
		connect();
		
		startCheckTask();
		
	}
	
	/**
	 * 启动检查任务
	 * 
	 * @author zai
	 * 2020-02-10 18:58:31
	 */
	public void startCheckTask() {
		this.config.getTaskExecutor().scheduleWithFixedDelay(5, 10, TimeUnit.SECONDS, () -> {
			checkAndUpdateService();
		});
	}
	
	/**
	 * 检查并更新服务信息到注册中心
	 * 
	 * @author zai
	 * 2020-02-10 19:00:52
	 */
	public void	checkAndUpdateService() {
		AtomicInteger extraDataUpdateTimes = config.getCustomDataUpdateTimes();
		int times = extraDataUpdateTimes.get();
		if (times > 0) {
			this.updateService();
			extraDataUpdateTimes.getAndAdd(-times);
		}
	}
	
	public void connect() {
		GGClient ggClient = config.getGGclient();
		RegistryInfo registry = config.getRegistryManager().getRandomRegistry();
		ggClient.connect(registry.getDomain(), registry.getPort())
		.addListener(f -> {
			if (!f.isSuccess()) {
				//连接失败，进行进行重连操作
				GGLoggerUtil.getLogger(this).info("Discovery Client Connect Server[{}:{}] Failed!",registry.getDomain(), registry.getPort());
				ggClient.schedule(config.getTryRegisterInterval(), () -> {
					connect();
				});
				return;
			}
			GGLoggerUtil.getLogger(this).info("Discovery Client Connect Server[{}:{}] Successfully!",registry.getDomain(), registry.getPort());
		});
	}
	
	/**
	 * 更新服务
	 * 
	 * @author zai
	 * 2020-02-04 17:11:08
	 */
	private void updateService() {
		GGSession session = config.getSession();
		if (session == null) {
			return;
		}
		DiscoveryServiceUpdateReq req = new DiscoveryServiceUpdateReq();
		
		ServiceInfo serviceInfo = new ServiceInfo();
		
		serviceInfo.setRegion(config.getRegion());
		serviceInfo.setZone(config.getZone());
		serviceInfo.setServiceId(config.getServiceId());
		serviceInfo.setServiceName(config.getServiceName());
		
		serviceInfo.setCustomData(config.getCustomData());
		
		req.setServiceInfo(serviceInfo);
		session.send(req);

	}
	
	public void syncServiceInfos() {
		GGSession session = config.getSession();
		if (session != null) {
			session.send(DiscoveryServiceListReq.DEFAULT_INSTANT);
		}
	}
	
	public void addRegisterSuccessListener(IClientRegisterSuccessListener listener) {
		this.registerSuccessListeners.add(listener);
	}
	
	public List<IClientRegisterSuccessListener> getRegisterSuccessListeners() {
		return registerSuccessListeners;
	}
	
	
	public DiscoveryClientConfig getConfig() {
		return config;
	}

}
