package com.ggx.admin.server.listen.basic;

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
import com.ggx.admin.server.model.ServiceInfoSessionListener;
import com.ggx.core.common.executor.SingleThreadTaskExecutor;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.session.GGSession;
import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;

/**
 * 基础信息监听管理器
 *
 * @author zai 2020-07-17 17:28:04
 */
public abstract class BasicSessionListenerManager implements ExecutorSupport{

	protected static final TaskExecutor TASK_EXECUTOR = new SingleThreadTaskExecutor("Info-Listen-Manager-");

	protected Map<GGSession, ServiceInfoSessionListener> sessionListenrs = new ConcurrentHashMap<>();

	
	
	@Override
	public TaskExecutor getTaskExecutor() {
		return TASK_EXECUTOR;
	}



	/**
	 * 添加或更新监听器
	 *
	 * @param session
	 * @author zai 2020-07-17 17:27:57
	 */
	public void addOrUpdateListener(ServiceInfoSessionListener sessionListener) {
		ServiceInfoSessionListener old = sessionListenrs.get(sessionListener.getSession());
		if (old == null || sessionListener.getServiceId() != old.getServiceId()) {
			old = sessionListenrs.put(sessionListener.getSession(), sessionListener);
			if (old == null) {
				sessionListener.getSession().addDisconnectListener(f -> {
					sessionListenrs.remove(sessionListener.getSession());
				});
			}
		}
	}

}
