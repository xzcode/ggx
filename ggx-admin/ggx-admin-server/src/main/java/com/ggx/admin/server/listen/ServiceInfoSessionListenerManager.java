package com.ggx.admin.server.listen;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ggx.admin.collector.server.GGXAdminCollectorServer;
import com.ggx.admin.collector.server.service.ServerDataService;
import com.ggx.admin.common.collector.data.model.server.ServerData;
import com.ggx.admin.server.handler.registry.model.resp.ServerDataModel;
import com.ggx.admin.server.handler.registry.model.resp.ServiceDataModel;
import com.ggx.admin.server.handler.service.model.resp.ListenServiceInfoResp;
import com.ggx.admin.server.model.SessionServiceListener;
import com.ggx.core.common.executor.SingleThreadTaskExecutor;
import com.ggx.core.common.executor.TaskExecutor;
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
public class ServiceInfoSessionListenerManager {

	private TaskExecutor taskExecutor = new SingleThreadTaskExecutor("Service-Info-Listen-Manager-");

	private Map<GGSession, SessionServiceListener> sessionListenrs = new ConcurrentHashMap<>();
	
	private ServiceManager serviceManager;
	
	private ServerDataService serverDataService;
	
	@Autowired
	private RegistryClient registryClient;
	
	@Autowired
	private GGXAdminCollectorServer ggxAdminCollectorServer;
	
	@PostConstruct
	public void init() {
		this.serviceManager = registryClient.getConfig().getServiceManager();
		this.serverDataService = ggxAdminCollectorServer.getConfig().getServerDataService();
	}

	/**
	 * 添加或更新监听器
	 *
	 * @param session
	 * @author zai 2020-07-17 17:27:57
	 */
	public void addOrUpdateListener(SessionServiceListener sessionListener) {
		SessionServiceListener old = sessionListenrs.get(sessionListener.getSession());
		if (old == null || sessionListener.getServiceId() != old.getServiceId()) {
			old = sessionListenrs.put(sessionListener.getSession(), sessionListener);
			if (old == null) {
				sessionListener.getSession().addDisconnectListener(f -> {
					sessionListenrs.remove(sessionListener.getSession());
				});
			}
		}
	}

	/**
	 * 启动超时检查任务
	 *
	 * @author zai
	 * 2020-07-17 17:43:31
	 */
	@PostConstruct
	public void startCheckTimeoutTask() {
		taskExecutor.scheduleWithFixedDelay(1000L, 10000L, TimeUnit.MILLISECONDS, () -> {
			if (sessionListenrs.isEmpty()) {
				return;
			}
			for (Entry<GGSession, SessionServiceListener> entry : sessionListenrs.entrySet()) {
				SessionServiceListener sessionListener = entry.getValue();
				if (sessionListener.isTimeout()) {
					this.sessionListenrs.remove(sessionListener.getSession());
				}
			}
		});
	}
	
	/**
	 * 启动发送服务信息任务
	 *
	 * @author zai
	 * 2020-07-17 17:52:18
	 */
	@PostConstruct
	public void startSendServiceInfoTask() {
		taskExecutor.scheduleWithFixedDelay(1000L, 1000L, TimeUnit.MILLISECONDS, () -> {
			if (sessionListenrs.isEmpty()) {
				return;
			}
			for (Entry<GGSession, SessionServiceListener> entry : sessionListenrs.entrySet()) {
				SessionServiceListener sessionListener = entry.getValue();
				this.sendListenServiceInfo(sessionListener);
			}
		});
	}
	
	/**
	 * 发送监听服务信息
	 *
	 * @param sessionListener
	 * @author zai
	 * 2020-07-17 18:12:41
	 */
	public void sendListenServiceInfo(SessionServiceListener sessionListener) {
		GGSession session = sessionListener.getSession();
		
		ServiceInfo serviceInfo = serviceManager.getService(sessionListener.getServiceId());
		ServerData serverData = serverDataService.getData(sessionListener.getServiceId());
		
		session.send(new ListenServiceInfoResp(ServiceDataModel.create(serviceInfo), ServerDataModel.create(serverData)));
	}

}
