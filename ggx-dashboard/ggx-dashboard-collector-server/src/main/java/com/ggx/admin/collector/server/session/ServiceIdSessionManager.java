package com.ggx.admin.collector.server.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.session.GGSession;

/**
 * 服务id 会话映射管理器
 *
 * @author zai
 * 2020-07-20 20:11:46
 */
public class ServiceIdSessionManager {

	protected Map<String, GGSession> serviceIdSessionMap = new ConcurrentHashMap<>();

	/**
	 * 添加会话
	 *
	 * @param serviceId
	 * @param session
	 * @author zai
	 * 2020-07-20 20:12:07
	 */
	public void addSession(String serviceId, GGSession session) {
		GGSession old = serviceIdSessionMap.put(serviceId, session);
		if (old == null) {
			session.addDisconnectListener(s -> {
				serviceIdSessionMap.remove(serviceId, session);
			});
		}
	}
	
	public GGSession getSession(String serviceId) {
		return this.serviceIdSessionMap.get(serviceId);
	}

}
