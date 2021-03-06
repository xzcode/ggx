package com.ggx.registry.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ggx.core.client.GGClient;
import com.ggx.core.client.config.GGClientConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.registry.client.config.RegistryClientConfig;
import com.ggx.registry.client.events.ConnCloseEventListener;
import com.ggx.registry.client.events.ConnOpenEventListener;
import com.ggx.registry.client.handler.AddServiceRespHandler;
import com.ggx.registry.client.handler.RegisterRespHandler;
import com.ggx.registry.client.handler.ServiceListRespHandler;
import com.ggx.registry.client.handler.ServiceUnregisterRespHandler;
import com.ggx.registry.client.handler.ServiceUpdateRespHandler;
import com.ggx.registry.client.listener.ClientRegisterSuccessListener;
import com.ggx.registry.client.registry.RegistryInfo;
import com.ggx.registry.common.message.req.RegistryServiceListReq;
import com.ggx.registry.common.message.req.RegistryServiceUpdateReq;
import com.ggx.registry.common.message.resp.RegistryAddServiceResp;
import com.ggx.registry.common.message.resp.RegistryServiceListResp;
import com.ggx.registry.common.message.resp.RegistryServiceRegisterResp;
import com.ggx.registry.common.message.resp.RegistryServiceUnregisterResp;
import com.ggx.registry.common.message.resp.RegistryServiceUpdateResp;
import com.ggx.registry.common.service.ServiceInfo;

import io.netty.channel.nio.NioEventLoopGroup;

public class RegistryClient {
	
	private RegistryClientConfig config;
	
	protected List<ClientRegisterSuccessListener> registerSuccessListeners = new ArrayList<>();
	
	
	public RegistryClient(RegistryClientConfig config) {
		this.config = config;
		this.config.setRegistryClient(this);
	}

	public void start() {
		GGClientConfig ggConfig = new GGClientConfig();
		ggConfig.setPingPongEnabled(true);
		ggConfig.setPrintPingPongInfo(config.isPrintPingPongInfo());
		ggConfig.setWorkerGroup(new NioEventLoopGroup(1, new GGThreadFactory("registry-client-", false)));
		ggConfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggConfig.getPackLogger().addPackLogFilter(pack -> {
			return this.config.isShowRegistryLog();
		});
		ggConfig.init();
		
		GGClient ggClient = new GGClient(ggConfig);
		config.setGGclient(ggClient);
		
		ggClient.onMessage(RegistryServiceRegisterResp.ACTION_ID, new RegisterRespHandler(config));
		ggClient.onMessage(RegistryServiceListResp.ACTION_ID, new ServiceListRespHandler(config));
		ggClient.onMessage(RegistryServiceUpdateResp.ACTION_ID, new ServiceUpdateRespHandler(config));
		ggClient.onMessage(RegistryServiceUnregisterResp.ACTION_ID, new ServiceUnregisterRespHandler(config));
		ggClient.onMessage(RegistryAddServiceResp.ACTION_ID, new AddServiceRespHandler(config));
		
		ggClient.addEventListener(GGEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		ggClient.addEventListener(GGEvents.Connection.OPENED, new ConnOpenEventListener(config));
		
		
		ggClient.scheduleWithFixedDelay(30L * 1000L, 30L * 1000L, TimeUnit.MILLISECONDS, () -> {
			GGSession session = config.getSession();
			if (config.isRequireServices() && session != null && !session.isExpired()) {
				config.getSession().send(RegistryServiceListReq.DEFAULT_INSTANT);				
			}
		});
		
		connect();
	}
	
	
	public void addCustomData(String key, String value) {
		this.config.addCustomData(key, value);
		this.updateService();
	}
	
	public void connect() {
		GGClient ggClient = config.getGGclient();
		ggClient.schedule(3000L, () -> {
			RegistryInfo registry = config.getRegistryManager().getRandomRegistry();
			ggClient.connect(registry.getDomain(), registry.getPort())
			.addListener(f -> {
				if (!f.isSuccess()) {
					//连接失败，进行进行重连操作
					GGLoggerUtil.getLogger(this).warn("Registry Client Connect Server[{}:{}] Failed!",registry.getDomain(), registry.getPort());
					ggClient.schedule(config.getTryRegisterInterval(), () -> {
						connect();
					});
					return;
				}
				GGLoggerUtil.getLogger(this).warn("Registry Client Connect Server[{}:{}] Successfully!",registry.getDomain(), registry.getPort());
			});
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
		RegistryServiceUpdateReq req = new RegistryServiceUpdateReq();
		
		ServiceInfo serviceInfo = new ServiceInfo();
		
		serviceInfo.setRegion(config.getRegion());
		serviceInfo.setZone(config.getZone());
		serviceInfo.setServiceId(config.getServiceId());
		serviceInfo.setServiceGroupId(config.getServiceGroupId());
		serviceInfo.setServiceName(config.getServiceName());
		
		serviceInfo.setCustomData(config.getCustomData());
		
		req.setServiceInfo(serviceInfo);
		session.send(req);

	}
	
	public void addRegisterSuccessListener(ClientRegisterSuccessListener listener) {
		this.registerSuccessListeners.add(listener);
	}
	
	public List<ClientRegisterSuccessListener> getRegisterSuccessListeners() {
		return registerSuccessListeners;
	}
	
	
	public RegistryClientConfig getConfig() {
		return config;
	}
	public String getServiceId() {
		return config.getServiceId();
	}

}
