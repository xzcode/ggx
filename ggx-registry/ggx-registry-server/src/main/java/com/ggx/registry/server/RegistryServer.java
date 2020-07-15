package com.ggx.registry.server;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.executor.thread.GGThreadFactory;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.registry.common.constant.RegistryConstant;
import com.ggx.registry.common.message.req.RegistryServiceListReq;
import com.ggx.registry.common.message.req.RegistryServiceRegisterReq;
import com.ggx.registry.common.message.req.RegistryServiceUpdateReq;
import com.ggx.registry.common.service.ServiceGroup;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.registry.server.config.RegistryServerConfig;
import com.ggx.registry.server.constant.RegistryServerSessionKeys;
import com.ggx.registry.server.events.ConnActiveEventListener;
import com.ggx.registry.server.events.ConnCloseEventListener;
import com.ggx.registry.server.handler.RegisterReqHandler;
import com.ggx.registry.server.handler.ServiceListReqHandler;
import com.ggx.registry.server.handler.ServiceUpdateReqHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;
import com.xzcode.ggserver.core.server.impl.GGDefaultServer;

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
			selfService.addCustomData("REGISTRY_PORT", String.valueOf(this.config.getPort()));
			this.config.getServiceManager().registerService(selfService);
		}
		
		GGServerConfig ggConfig = new GGServerConfig();
		ggConfig.setPingPongEnabled(true);
		ggConfig.setPrintPingPongInfo(this.config.isPrintPingPongInfo());
		ggConfig.setProtocolType(ProtocolTypeConstants.TCP);
		ggConfig.setWorkThreadSize(this.config.getWorkThreadSize());
		ggConfig.setPort(this.config.getPort());
		ggConfig.setBossGroupThreadFactory(new GGThreadFactory("gg-registry-boss-", false));
		ggConfig.setWorkerGroupThreadFactory(new GGThreadFactory("gg-registry-worker-", false));
		ggConfig.getPackLogger().addPackLogFilter(pack -> {
			return this.config.isShowRegistryLog();
		});
		ggConfig.init();
		GGServer ggServer = new GGDefaultServer(ggConfig);
		
		ggServer.addEventListener(GGEvents.Connection.OPENED, new ConnActiveEventListener(config));
		
		ggServer.addEventListener(GGEvents.Connection.CLOSED, new ConnCloseEventListener(config));
		
		ggServer.onMessage(RegistryServiceRegisterReq.ACTION_ID, new RegisterReqHandler(config));
		ggServer.onMessage(RegistryServiceListReq.ACTION_ID, new ServiceListReqHandler(config));
		ggServer.onMessage(RegistryServiceUpdateReq.ACTION_ID, new ServiceUpdateReqHandler(config));
		
		config.setServer(ggServer);
		
		ggServer.start();
		
		this.startPeriodPrintServiceInfosTask();
		
		
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
				GGLoggerUtil.getLogger(this).warn(sb.toString());
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
