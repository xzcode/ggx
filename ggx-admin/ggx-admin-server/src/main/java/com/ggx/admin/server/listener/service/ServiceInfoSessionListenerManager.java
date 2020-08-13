package com.ggx.admin.server.listener.service;

import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ggx.admin.collector.server.GGXAdminCollectorServer;
import com.ggx.admin.collector.server.service.ServerDataService;
import com.ggx.admin.collector.server.session.ServiceIdSessionManager;
import com.ggx.admin.common.collector.data.model.server.ServerData;
import com.ggx.admin.common.collector.message.resp.CollectServiceDataResp;
import com.ggx.admin.server.handler.registry.model.resp.ServerDataModel;
import com.ggx.admin.server.handler.registry.model.resp.ServiceDataModel;
import com.ggx.admin.server.handler.service.model.resp.ListenServiceInfoResp;
import com.ggx.admin.server.listener.basic.BasicSessionListenerManager;
import com.ggx.admin.server.model.ServiceInfoSessionListener;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 服务信息监听管理器
 *
 * @author zai 2020-07-17 17:28:04
 */
@Component
public class ServiceInfoSessionListenerManager extends BasicSessionListenerManager {

	private ServiceManager serviceManager;

	private ServerDataService serverDataService;

	@Autowired
	private RegistryClient registryClient;

	@Autowired
	private GGXAdminCollectorServer ggxAdminCollectorServer;

	private ServiceIdSessionManager serviceIdSessionManager;

	@PostConstruct
	public void init() {
		this.serviceManager = registryClient.getConfig().getServiceManager();
		this.serverDataService = ggxAdminCollectorServer.getConfig().getServerDataService();
		this.serviceIdSessionManager = this.ggxAdminCollectorServer.getConfig().getServiceIdSessionManager();
	}

	/**
	 * 启动发送服务信息任务
	 *
	 * @author zai 2020-07-17 17:52:18
	 */
	@PostConstruct
	public void startSendServiceInfoTask() {
		scheduleWithFixedDelay(1000L, 1000L, TimeUnit.MILLISECONDS, () -> {
			if (sessionListenrs.isEmpty()) {
				return;
			}
			
			//只对有监听的服务发送信息获取请求
			for (Entry<String, AtomicInteger> entry : this.listenerCounters.entrySet()) {
				int listenerCount = entry.getValue().get();
				String serviceId = entry.getKey();
				
				if (listenerCount > 0) {
					GGSession serviceSession = serviceIdSessionManager.getSession(serviceId);
					serviceSession.send(CollectServiceDataResp.DEFAULT_INSTANCE);
				}
			}
			
			//发送信息给正在监听的客户端会话
			for (Entry<GGSession, ServiceInfoSessionListener> entry : sessionListenrs.entrySet()) {
				ServiceInfoSessionListener sessionListener = entry.getValue();
				this.sendListenServiceInfo(sessionListener);
			}
		});
	}

	/**
	 * 发送监听服务信息
	 *
	 * @param sessionListener
	 * @author zai 2020-07-17 18:12:41
	 */
	public void sendListenServiceInfo(ServiceInfoSessionListener sessionListener) {
		GGSession session = sessionListener.getSession();

		ServiceInfo serviceInfo = serviceManager.getService(sessionListener.getServiceId());
		ServerData serverData = serverDataService.getData(sessionListener.getServiceId());

		session.send(new ListenServiceInfoResp(ServiceDataModel.create(serviceInfo), ServerDataModel.create(serverData)));
	}

}
