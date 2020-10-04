package com.ggx.registry.server;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.core.server.impl.GGXDefaultCoreServer;
import com.ggx.registry.common.service.ServiceGroup;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.registry.server.events.ConnActiveEventListener;
import com.ggx.registry.server.events.ConnCloseEventListener;
import com.ggx.registry.server.events.ConnHeartbeatLostEventListener;
import com.ggx.registry.server.handler.RegisterReqHandler;
import com.ggx.registry.server.handler.ServiceListReqHandler;
import com.ggx.registry.server.handler.ServiceUpdateReqHandler;
import com.ggx.util.logger.GGXLogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RegistryServer {
	
	private RegistryServerConfig config;
	
	
	
	public RegistryServer(RegistryServerConfig config) {
		super();
		this.config = config;
	}

	public void start() {
		
		if (this.config.isRegisterSelf()) {
			ServiceInfo selfService = new ServiceInfo();
			selfService.setServiceGroupId(this.config.getServiceGroupId());
			selfService.setServiceId(this.config.getServiceId());
			selfService.setServiceName("registry-server");
			selfService.setServiceDescName(this.config.getServiceDescName());
			selfService.setServiceGroupDescName(this.config.getServiceGroupDescName());
			selfService.setHost(this.config.getHost());
			selfService.setPort(this.config.getPort());
			this.config.getServiceManager().registerService(selfService);
		}
		
		GGXCoreServerConfig ggconfig = new GGXCoreServerConfig();
		
		ggconfig.setPingPongEnabled(true);
		ggconfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		ggconfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggconfig.setWorkThreadSize(this.config.getWorkThreadSize());
		ggconfig.setPort(this.config.getPort());
		ggconfig.setBossGroupThreadFactory(new GGXThreadFactory("gg-registry-boss-", false));
		ggconfig.setWorkerGroupThreadFactory(new GGXThreadFactory("gg-registry-worker-", false));
		ggconfig.getPackLogger().addPackLogFilter(pack -> {
			return this.config.isShowRegistryLog();
		});
		ggconfig.setGgxComponent(true);
		ggconfig.init();
		GGXCoreServer ggServer = new GGXDefaultCoreServer(ggconfig);
		
		ggServer.addEventListener(GGXCoreEvents.Connection.OPENED, new ConnActiveEventListener(config));
		ggServer.addEventListener(GGXCoreEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		ggServer.addEventListener(GGXCoreEvents.HeartBeat.LOST, new ConnHeartbeatLostEventListener(config));
		
		ggServer.onMessage(new RegisterReqHandler(config));
		ggServer.onMessage(new ServiceListReqHandler(config));
		ggServer.onMessage(new ServiceUpdateReqHandler(config));
		
		config.setServer(ggServer);
		
		ggServer.start();
		
		this.startPeriodPrintServiceInfosTask();
		
		
	}
	
	
	public void shutdown() {
		this.config.getServer().shutdown();
	}
	
	/**
	 * 周期性输出服务信息
	 *
	 * @author zai
	 * 2020-05-18 15:36:44
	 */
	private void startPeriodPrintServiceInfosTask() {
		if (this.config.isPeriodPrintServiceInfos()) {
			ServiceManager serviceManager = this.getConfig().getServiceManager();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			StringBuilder sb = new StringBuilder(1024);
			this.config.getServer().scheduleWithFixedDelay(10L, 10L, TimeUnit.SECONDS, () -> {
				int groupNum = 0;
				int serviceNum = 0;
				
				sb.append("\n").append("---------------- Services Infos ---------------- ").append("\n");
				List<ServiceGroup> serviceGroupList = serviceManager.getServiceGroupList();
				for (ServiceGroup serviceGroup : serviceGroupList) {
					groupNum++;
					sb
					.append("\n")
					.append("ServiceGroupName: \t")
					.append(serviceGroup.getServiceGroupId())
					.append("\n\n")
					;
					List<ServiceInfo> serviceList = serviceGroup.getServiceList();
					int i = 1;
					for (ServiceInfo serviceInfo : serviceList) {
						serviceNum++;
						sb
						.append("Service - ")
						.append(i++)
						.append(":\n")
						.append("ServiceName: \t").append(serviceInfo.getServiceName())
						.append("\n")
						.append("ServiceId:   \t").append(serviceInfo.getServiceId())
						.append("\n")
						.append("ServiceGroupId:   \t").append(serviceInfo.getServiceGroupId())
						.append("\n")
						.append("ServiceDescName:   \t").append(serviceInfo.getServiceDescName())
						.append("\n")
						.append("ServiceGroupDescName:   \t").append(serviceInfo.getServiceGroupDescName())
						.append("\n")
						.append("Host:        \t").append(serviceInfo.getHost())
						.append("\n")
						.append("CustomData:  \t").append(gson.toJson(serviceInfo.getCustomData()))
						.append("\n\n");
						
					}
					sb.append("\n-----------------------------------");
				}
				sb.append("\n");
				sb.append("Total Groups:   \t").append(groupNum).append("\n");
				sb.append("Total Services: \t").append(serviceNum).append("\n");
				sb.append("-----------------------------------\n");
				GGXLogUtil.getLogger(this).warn(sb.toString());
				sb.setLength(0);
			});
		}
	}
	
	public void setConfig(RegistryServerConfig config) {
		this.config = config;
	}
	
	public RegistryServerConfig getConfig() {
		return config;
	}
	
}
