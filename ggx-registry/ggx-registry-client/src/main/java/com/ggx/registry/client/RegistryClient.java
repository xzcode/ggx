package com.ggx.registry.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ggx.core.client.GGXCoreClient;
import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.session.GGXSession;
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
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.util.logger.GGXLogUtil;

import io.netty.channel.nio.NioEventLoopGroup;

public class RegistryClient {
	
	private RegistryClientConfig config;
	
	protected List<ClientRegisterSuccessListener> registerSuccessListeners = new ArrayList<>();
	
	
	
	private ServiceInfo cachedServiceInfo;
	
	public RegistryClient(RegistryClientConfig config) {
		this.config = config;
		this.config.setRegistryClient(this);
	}

	public void start() {
		
		
		GGXCoreClientConfig ggconfig = new GGXCoreClientConfig();
		ggconfig.setGgxComponent(true);
		ggconfig.setPingPongEnabled(true);
		ggconfig.setPrintPingPongInfo(config.isPrintPingPongInfo());
		ggconfig.setWorkerGroup(new NioEventLoopGroup(1, new GGXThreadFactory("registry-client-", false)));
		ggconfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggconfig.getPackLogger().addPackLogFilter(pack -> {
			return this.config.isShowRegistryLog();
		});
		ggconfig.init();
		
		GGXCoreClient ggClient = new GGXCoreClient(ggconfig);
		config.setGGclient(ggClient);
		
		ggClient.registerMessageController(new RegisterRespHandler(config));
		ggClient.registerMessageController(new ServiceListRespHandler(config));
		ggClient.registerMessageController(new ServiceUpdateRespHandler(config));
		ggClient.registerMessageController(new ServiceUnregisterRespHandler(config));
		ggClient.registerMessageController(new AddServiceRespHandler(config));
		
		ggClient.addEventListener(GGXCoreEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		ggClient.addEventListener(GGXCoreEvents.Connection.OPENED, new ConnOpenEventListener(config));
		
		
		ggClient.scheduleWithFixedDelay(30L * 1000L, 30L * 1000L, TimeUnit.MILLISECONDS, () -> {
			GGXSession session = config.getSession();
			if (config.isRequireServices() && session != null && !session.isExpired()) {
				config.getSession().send(RegistryServiceListReq.ALL_SERVICE_INSTANT);				
			}
		});
		
		connect();
	}
	
	public void shutdown() {
		if (this.config.getCoreClient() != null) {
			this.config.getCoreClient().shutdown();			
		}
	}
	
	
	public void addCustomData(String key, String value) {
		this.config.addCustomData(key, value);
		this.updateService();
	}
	
	public void addCustomData(String key, String value, boolean updateService) {
		this.config.addCustomData(key, value);
		if (updateService) {
			this.updateService();
		}
	}
	
	public void connect() {
		GGXCoreClient ggClient = config.getCoreClient();
		ggClient.schedule(3000L, () -> {
			RegistryInfo registry = config.getRegistryManager().getRandomRegistry();
			ggClient.connect(registry.getDomain(), registry.getPort())
			.addListener(f -> {
				if (!f.isSuccess()) {
					//连接失败，进行进行重连操作
					GGXLogUtil.getLogger(this).warn("Registry Client Connect Server[{}:{}] Failed!",registry.getDomain(), registry.getPort());
					ggClient.schedule(config.getTryRegisterInterval(), () -> {
						connect();
					});
					return;
				}
				GGXLogUtil.getLogger(this).warn("Registry Client Connect Server[{}:{}] Successfully!",registry.getDomain(), registry.getPort());
			});
		});
		
	}
	
	/**
	 * 更新服务
	 * 
	 * @author zai
	 * 2020-02-04 17:11:08
	 */
	public void updateService() {
		GGXSession session = config.getSession();
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
		
		serviceInfo.setServiceDescName(config.getServiceDescName());
		serviceInfo.setServiceGroupDescName(config.getServiceGroupDescName());
		
		serviceInfo.setPort(this.config.getPort());
		
		serviceInfo.setCustomData(config.getCustomData());
		
		this.cachedServiceInfo = serviceInfo;
		
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
	
	public ServiceInfo getCachedServiceInfo() {
		return cachedServiceInfo;
	}

}
