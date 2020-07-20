package com.ggx.admin.server.listener.basic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.admin.server.model.ServiceInfoSessionListener;
import com.ggx.core.common.executor.SingleThreadTaskExecutor;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.session.GGSession;

/**
 * 基础信息监听管理器
 *
 * @author zai 2020-07-17 17:28:04
 */
public abstract class BasicSessionListenerManager implements ExecutorSupport {

	protected TaskExecutor taskExecutor;

	protected Map<GGSession, ServiceInfoSessionListener> sessionListenrs = new ConcurrentHashMap<>();

	protected Map<String, AtomicInteger> listenerCounters = new ConcurrentHashMap<>();

	@Override
	public TaskExecutor getTaskExecutor() {
		synchronized (this) {

			if (this.taskExecutor == null) {
				this.taskExecutor = new SingleThreadTaskExecutor("Info-Listener-Manager-");
			}
		}
		return this.taskExecutor;
	}

	/**
	 * 添加或更新监听器
	 *
	 * @param session
	 * @author zai 2020-07-17 17:27:57
	 */
	public void addOrUpdateListener(ServiceInfoSessionListener sessionListener) {
		submitTask(() -> {
			ServiceInfoSessionListener old = sessionListenrs.get(sessionListener.getSession());
			if (old == null) {
				old = sessionListenrs.put(sessionListener.getSession(), sessionListener);
				sessionListener.getSession().addDisconnectListener(f -> {
					submitTask(() -> {
						sessionListenrs.remove(sessionListener.getSession());
						AtomicInteger count = listenerCounters.get(sessionListener.getServiceId());
						count.decrementAndGet();
						if(count.get() <= 0) {
							listenerCounters.remove(sessionListener.getServiceId());
						}
					});
				});
			}
			AtomicInteger count = listenerCounters.get(sessionListener.getServiceId());
			if (count != null) {
				count.incrementAndGet();
			} else {
				listenerCounters.put(sessionListener.getServiceId(), new AtomicInteger(1));
			}
		});
	}
	
	/**
	 * 获取服务信息
	 *
	 * @param serviceId
	 * @return
	 * @author zai
	 * 2020-07-20 19:12:59
	 */
	public int getListenerCount(String serviceId) {
		AtomicInteger count = this.listenerCounters.get(serviceId);
		if (count != null) {
			return count.get();
		}
		return 0;
	}

}
